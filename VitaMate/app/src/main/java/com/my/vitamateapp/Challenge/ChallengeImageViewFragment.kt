package com.my.vitamateapp.Challenge

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.kakao.sdk.friend.m.v
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentChallengeImageViewBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChallengeImageViewFragment : Fragment() {
    private var _binding: FragmentChallengeImageViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var filePath: String // 파일 경로를 저장할 변수 선언

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true //옵션만 설정하고자 true로 지정
        try {
            var inputStream = requireContext().contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, option)
            inputStream!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val (height: Int, width: Int) = option.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeImageViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 갤러리 선택 로직
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options().apply { inSampleSize = calRatio }

                val inputStream = requireContext().contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream?.close()

                bitmap?.let {
                    binding.buttonGallery.setImageBitmap(bitmap)
                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.buttonGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        // 카메라 선택 로직
        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options().apply { inSampleSize = calRatio }

            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.buttonCamera.setImageBitmap(bitmap)
            }
        }

        binding.buttonCamera.setOnClickListener {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filePath = file.absolutePath

            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.my.vitamateapp.fileprovider",
                file
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



//    private var _binding: FragmentChallengeImageViewBinding? = null
//    private val binding get() = _binding!!
//
//    private val CAMERA_CODE = 100
//    private val STORAGE_CODE = 101
//    private val CAMERA = arrayOf(Manifest.permission.CAMERA)
//    private val STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//
//    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
//    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Fragment 바인딩 설정
//        _binding = FragmentChallengeImageViewBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // 카메라 버튼 클릭
//        binding.buttonCamera.setOnClickListener {
//            CallCamera()
//        }
//
//        // 갤러리 버튼 클릭
//        binding.buttonGallery.setOnClickListener {
//            GetAlbum()
//        }
//
//        // Activity ResultLauncher 설정
//        // 카메라에서 찍은 이미지의 URI를 저장
//        cameraLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val bitmap = result.data?.extras?.get("data") as? Bitmap
//                    bitmap?.let {
//                        val uri = saveFile(RandomFileName(), "image/jpeg", it)
//                        if (uri != null) {
//                            // ChallengeBottomSheetDialog2Fragment로 URI 전달
//                            val bundle = Bundle().apply {
//                                putString("imageUri", uri.toString())
//                            }
//                            val dialogFragment = ChallengeBottomSheetDialog2Fragment().apply {
//                                arguments = bundle
//                            }
//                            dialogFragment.show(parentFragmentManager, dialogFragment.tag)
//                        }
//                    }
//                }
//            }
//
//// 갤러리에서 선택한 이미지의 URI를 저장
//        galleryLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val uri = result.data?.data
//                    uri?.let {
//                        // ChallengeBottomSheetDialog2Fragment로 URI 전달
//                        val bundle = Bundle().apply {
//                            putString("imageUri", it.toString())
//                        }
//                        val dialogFragment = ChallengeBottomSheetDialog2Fragment().apply {
//                            arguments = bundle
//                        }
//                        dialogFragment.show(parentFragmentManager, dialogFragment.tag)
//                    }
//                }
//            }
//    }
//
//
//        // 카메라 호출 - 권한 체크 후 호출
//    fun CallCamera() {
//        if (checkPermission(CAMERA, CAMERA_CODE) && checkPermission(STORAGE, STORAGE_CODE)) {
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            cameraLauncher.launch(cameraIntent)
//        }
//    }
//
//    // 갤러리 호출
//    fun GetAlbum() {
//        if (checkPermission(STORAGE, STORAGE_CODE)) {
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            galleryIntent.type = "image/*"
//            galleryLauncher.launch(galleryIntent)
//        }
//    }
//
//    // 권한 요청 결과 처리
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            CAMERA_CODE -> {
//                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                    CallCamera()
//                } else {
//                    Toast.makeText(requireContext(), "카메라 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
//                }
//            }
//            STORAGE_CODE -> {
//                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                    GetAlbum()
//                } else {
//                    Toast.makeText(requireContext(), "저장소 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    // 권한 체크 및 요청
//    fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            for (permission in permissions) {
//                if (ContextCompat.checkSelfPermission(requireContext(), permission)
//                    != PackageManager.PERMISSION_GRANTED
//                ) {
//                    ActivityCompat.requestPermissions(requireActivity(), permissions, type)
//                    return false
//                }
//            }
//        }
//        return true
//    }
//
//    // 사진 저장
//    fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri? {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                put(MediaStore.Images.Media.IS_PENDING, 1)
//            }
//        }
//
//        val uri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            val descriptor = requireActivity().contentResolver.openFileDescriptor(it, "w")
//            descriptor?.use { fd ->
//                FileOutputStream(fd.fileDescriptor).use { fos ->
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//                }
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                contentValues.clear()
//                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
//                requireActivity().contentResolver.update(uri, contentValues, null, null)
//            }
//        }
//
//        return uri
//    }
//
//
//
//    // 랜덤 파일명 생성
//    fun RandomFileName(): String {
//        return SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//
//
//}
