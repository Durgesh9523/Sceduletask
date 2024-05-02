package com.example.schedultask
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ProjectDetail : AppCompatActivity() {
    private lateinit var viewModel: ViewModel
    private lateinit var uri: Uri
    private val REQUEST_PHONE_CALL = 1
    private val UPDATE_REQUEST_CODE = 101
    private var id=0
    private var phoneNumber = "123456789"
    companion object {
        const val REQUEST_PERMISSION_CODE = 100
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val name: TextView = findViewById(R.id.Name)
        val title: TextView=findViewById(R.id.title)
        val canName:TextView=findViewById(R.id.can_name)
        val canEmail:TextView=findViewById(R.id.can_email)
        val canPhone:TextView=findViewById(R.id.can_phone)
        val interset:TextView=findViewById(R.id.interest)
        val website:TextView=findViewById(R.id.can_Website)
        val discussion:TextView=findViewById(R.id.CandidateChat)
        id = intent.getIntExtra("item_id", -1)
        Log.d("another id: ",id.toString())
        val call_button:LinearLayout=findViewById(R.id.call)
        call_button.setOnClickListener {
            makeCall();
        }
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getCandidateById(id).observe(this, Observer { candidate ->
            candidate?.let { // Check if candidate is not null
                name.text = it.candidateName ?: ""
                title.text = it.candidateSpecial ?: ""
                canName.text = it.candidateName ?: ""
                canEmail.text = it.candidateEmail ?: ""
                Log.d("Email is: ", it.candidateEmail ?: "")
                canPhone.text = it.candidatePhone?.toString() ?: ""
                interset.text = it.isInterseted ?: ""
                website.text = it.website ?: ""
                discussion.text = it.discussionDetails ?: ""
                phoneNumber = it.candidatePhone?.toString() ?: ""
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openFile(uriString: String) {
        val uri: Uri = Uri.parse(uriString)
        val contentUri: Uri? = MediaStore.getDocumentUri(this, uri)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(contentUri, contentUri?.let { getMimeType(it) })
        if (intent.resolveActivity(packageManager) != null) {
            grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (contentUri != null) {
                contentResolver.takePersistableUriPermission(contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app to handle this file type", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun getMimeType(uri: Uri): String? {
        return contentResolver.getType(uri)
    }
    private fun makeCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
                } else {
                    startActivity(callIntent)
                }
            } else {
                startActivity(callIntent)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                val intent: Intent = Intent(this, CandidateDetail::class.java)
                Log.d("id is",id.toString())
                intent.putExtra("candidate_id", id)
                startActivity(intent)
                true
            }
            R.id.action_delete -> {
                showConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the phone call
                makeCall()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to proceed?")

        // Set positive button and its click listener
        builder.setPositiveButton("Yes") { dialog, which ->
            performAction()
        }

        // Set negative button and its click listener
        builder.setNegativeButton("No") { dialog, which ->
            // Do nothing or perform another action when user clicks "No"
            // For example, dismiss the dialog or cancel the operation
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun performAction() {
        viewModel.deleteCandidateById(id)
        finish()
    }
}