package com.example.schedultask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class RequirementScreen : AppCompatActivity() {
    private lateinit var viewModel: ViewModel
    private val UPDATE_REQUEST_CODE = 101
    private var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requirement_screen)
        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val name:TextView=findViewById(R.id.Name)
        val tile:TextView=findViewById(R.id.title)
        val desc:TextView=findViewById(R.id.desc)
        val bussi:TextView=findViewById(R.id.requirement)
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        id = intent.getIntExtra("item_id", -1)
        viewModel.getRequirementById(id).observe(this, Observer { requirement->
            requirement?.let {
                name.text = requirement.requirementName
                tile.text = requirement.requirementSpecial
                desc.text = requirement.requirementd
                Log.d("requirement desc", requirement.requirementd)
                bussi.text = requirement.businessRequ
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                val intent: Intent = Intent(this,RequirementDetails::class.java)
                intent.putExtra("requirement_id", id)
                Log.d("id requirement",id.toString())
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
    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to proceed?")

        // Set positive button and its click listener
        builder.setPositiveButton("Yes") { dialog, which ->
            performAction()
            finish()
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
        viewModel.deleteRequirementById(id)
    }
}