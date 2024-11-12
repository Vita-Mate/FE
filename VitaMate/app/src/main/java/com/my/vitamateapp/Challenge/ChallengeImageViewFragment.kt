package com.my.vitamateapp.Challenge

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.my.vitamateapp.databinding.FragmentChallengeImageViewBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChallengeImageViewFragment : Fragment() {
    private var _binding: FragmentChallengeImageViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var filePath: String
    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_STORAGE_PERMISSION = 101
    private val TAG = "ChallengeImageViewFragment"

    private lateinit var requestCameraFileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate 호출됨")
        super.onCreate(savedInstanceState)
        requestCameraFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "사진 촬영 성공: $filePath")
                handleBitmap(filePath)
            } else {
                Log.e(TAG, "사진 촬영 실패")
                Toast.makeText(requireContext(), "사진 촬영 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChallengeImageViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        // 카메라 권한 확인
        checkPermissions()

        // 카메라 버튼 클릭 리스너
        binding.buttonCamera.setOnClickListener {
            Log.d(TAG, "카메라 버튼 클릭")
            requestCameraPermissionAndTakePicture()
        }

        // 텍스트뷰 클릭 리스너
        binding.textCamera.setOnClickListener {
            Log.d(TAG, "텍스트 클릭")
            requestCameraPermissionAndTakePicture()
        }
    }

    private fun requestCameraPermissionAndTakePicture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "카메라 권한 요청")
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            // 카메라 권한이 이미 허용된 경우 사진 촬영
            takePicture()
        }
    }

    private fun checkPermissions() {
        Log.d(TAG, "권한 확인 중...")
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "저장소 권한 요청")
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        }
    }

    private fun takePicture() {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        filePath = file.absolutePath

        Log.d(TAG, "사진 파일 경로: $filePath")

        val photoURI: Uri = FileProvider.getUriForFile(requireContext(), "com.my.vitamateapp.fileprovider", file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        Log.d(TAG, "사진 촬영 인텐트 실행")
        requestCameraFileLauncher.launch(intent) // ActivityResultLauncher 사용
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "카메라 권한이 허용되었습니다.")
                    takePicture()
                } else {
                    Log.e(TAG, "카메라 권한이 거부되었습니다.")
                    Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "저장소 권한이 허용되었습니다.")
                } else {
                    Log.e(TAG, "저장소 권한이 거부되었습니다.")
                    Toast.makeText(requireContext(), "저장소 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleBitmap(filePath: String) {
        // 비트맵 처리 로직 구현
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 바인딩 해제
        Log.d(TAG, "onDestroyView 호출")
    }
}


