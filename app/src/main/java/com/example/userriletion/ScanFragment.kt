package com.example.userriletion

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.userriletion.databinding.FragmentScanBinding
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ScanFragment : Fragment() {
    companion object {
        const val TAG = "TFLite -ODP"
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CODE = 42
    }

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var photoFile: File? = null
    private lateinit var ImageUri: Uri




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takephoto.setOnClickListener {

            dispatchTakePictureIntent()

        }
        binding.selectimage.setOnClickListener {
            selectImage()
        }

    }



    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    photoFile = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        context?.let { context ->
                            val photoURI: Uri = FileProvider.getUriForFile(
                                context,
                                "com.example.userriletion.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data !=null) {
//            Log.d("fotoBodoh", "Jalan ${data?.data!!}")
//
//                ImageUri = data.data!!
//            binding.image.setImageURI(ImageUri)

            val imageBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
            binding.image.setImageBitmap(imageBitmap)
            ImageUri = Uri.fromFile(File(photoFile?.absolutePath))

            var reducedBitmap = reducedImageBitmap(imageBitmap)
            val gambarSiapDideteksi = getCapturedImage(reducedBitmap)
            runObjectDetection(gambarSiapDideteksi)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }
    private fun reducedImageBitmap(imageBitmap: Bitmap): Bitmap {
        var width = imageBitmap.width
        var height = imageBitmap.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = 1000
            height = (width / bitmapRatio).toInt()
        } else {
            height = 1000
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(imageBitmap, width, height, true)
    }
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
    private fun getCapturedImage(reducebitmap: Bitmap): Bitmap {
        val exifInterface = ExifInterface(photoFile?.absolutePath!!)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(reducebitmap, 90f)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(reducebitmap, 180f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(reducebitmap, 270f)
            }
            else -> {
                reducebitmap
            }
        }
    }
    private fun runObjectDetection(bitmap: Bitmap) {
        val image = TensorImage.fromBitmap(bitmap)
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(4)
            .setScoreThreshold(0.5f)
            .build()

        val detector = ObjectDetector.createFromFileAndOptions(
            requireContext(),
            "lite-model_efficientdet_lite0_detection_metadata_1.tflite",
            options
        )

        val result = detector.detect(image)
        // runOnUiThread {
        //   image.setImageBitmap(imgWithResult)
        //  }
        //debugPrint(result)

    }
    private fun drawDetectionResult(
        bitmap: Bitmap,
        detectionResults: List<DetectionResult>
    ): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        detectionResults.forEach {
            pen.color = Color.RED
            pen.strokeWidth = 8F
            pen.style = Paint.Style.STROKE
            val box = it.boundingBox
            canvas.drawRect(box, pen)

            val tagSize = Rect(0, 0, 0, 0)

            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 2F

            pen.textSize = 96f
            pen.getTextBounds(it.text, 0, it.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()

            if (fontSize < pen.textSize) pen.textSize = fontSize

            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
            canvas.drawText(
                it.text, box.left + margin,
                box.top + tagSize.height().times(1F), pen
            )
        }
        return outputBitmap
    }
//    private fun debugPrint(result: List<Detection>) {
//        for ((i, obj) in result.withIndex()) {
//            val box = boundingBox
//            Log.d(ContentValues.TAG, "Detection Object: ${i}")
//            Log.d(ContentValues.TAG, "boundIBox: (${box.left}, ${box.top}")
//            for ((j, category) in obj.categories.withIndex()) {
//                Log.d(ContentValues.TAG, "label $j:${category.label}")
//                val confidence: Int = category.score.times(100).toTensorImage
//                Log.d(ContentValues.TAG, "confidence : ${confidence}%")
//
//            }
//        }
//    }
}
data class DetectionResult(val boundingBox: RectF, val text: String)

