package com.example.schedultask

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RequirementDetails : AppCompatActivity() {
    private var requirement: Requirement? = null
     private var  selectedFilePath: String=""
    private val PICK_FILE_REQUEST_CODE = 100
    lateinit var fileChoosen:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requirement_details)

        val btn: Button = findViewById(R.id.btn)
        val Name: EditText = findViewById(R.id.ProjectName)
        val Spec: EditText = findViewById(R.id.ProjectSpec)
        val Details: EditText = findViewById(R.id.ProjectDesc)
        val bussiness:EditText=findViewById(R.id.BusinessReq)
        val upload: ImageView = findViewById(R.id.choosefile)
        fileChoosen= findViewById(R.id.fileChoosen)
        upload.setOnClickListener {
            openFilePicker()
        }
        val requirementId = intent.getIntExtra("requirement_id", -1)
        val RequirementDao = AppDatabase.getInstance(applicationContext).requirementDetailDao()
        Log.d("Requirement id:",requirementId.toString())
        if (requirementId != -1) {
            // Observe the LiveData returned by getCandidateById
            RequirementDao.getRequirementById(requirementId).observe(this) { requirement ->
                requirement?.let {
                    // Assign the fetched candidate data to the candidate variable
                    this.requirement = requirement
                    Name.setText(requirement.requirementName)
                    Spec.setText(requirement.requirementSpecial)
                    Details.setText(requirement.requirementd)
                    bussiness.setText(requirement.businessRequ)
                    fileChoosen.text=""
                }
            }
        }
        btn.setOnClickListener {
            val requirementName = Name.text.toString()
            val requirementSpec = Spec.text.toString()
            val requirementDetails = Details.text.toString()
            val busineess=bussiness.text.toString()
            val empty_Requirement=Requirement(
                requirementName="",
                requirementSpecial = "",
                requirementd = "",
                businessRequ = "",
                fileUrl = ""
            )
            val updatedRequirement = requirement ?: empty_Requirement// Default values for new candidate
            updatedRequirement.apply {
                this.requirementName =requirementName
                this.requirementSpecial = requirementSpec
                this.requirementd = requirementDetails
                this.businessRequ = busineess
                this.fileUrl = selectedFilePath
            }
            GlobalScope.launch(Dispatchers.IO) {
                if (requirementId != -1) {
                    // Update the existing candidate if candidateId is valid
                    RequirementDao.updateRequirement(updatedRequirement)
                } else {
                    // Insert a new candidate if candidateId is not valid
                    RequirementDao.insertRequirement(updatedRequirement)
                }
            }
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedFilePath", selectedFilePath)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // You can specify a specific MIME type here if needed
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val (fileUri, fileName) = getFileInfoFromUri(uri)
                if (fileUri != null && fileName != null) {
                    Log.d("Selected File URI", fileUri.toString())
                    Log.d("Selected File Name", fileName)
                    selectedFilePath = fileUri.toString()
                    fileChoosen.text = fileName
                } else {
                    Log.e("Selected File Info", "Failed to get file info")
                    // Handle null values here, set a default file or show an error message
                    selectedFilePath = ""
                    fileChoosen.text = "No file chosen"
                }
            }
        }
    }
    private fun getFileInfoFromUri(uri: Uri): Pair<Uri?, String?> {
        var fileUri: Uri? = null
        var fileName: String? = null

        val contentResolver = applicationContext.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                // Get the file URI
                fileUri = uri

                // Get the display name of the file
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(displayNameIndex)
            }
        }
        return Pair(fileUri, fileName)
    }

}
