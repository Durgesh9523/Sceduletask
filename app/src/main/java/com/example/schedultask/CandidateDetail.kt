    package com.example.schedultask

    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.EditText
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.GlobalScope
    import kotlinx.coroutines.launch

    class CandidateDetail : AppCompatActivity() {
        private var candidate: Candidate? = null // Declare a nullable variable to hold candidate data

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_candidate_detail)

            val btn: Button = findViewById(R.id.btn)
            val Name: EditText = findViewById(R.id.candidateName)
            val Spec: EditText = findViewById(R.id.candidateSpecial)
            val Details: EditText = findViewById(R.id.discussionDetails)
            val Email: EditText = findViewById(R.id.candidateEmail)
            val phone: EditText = findViewById(R.id.candidatePhone)
            val website: EditText = findViewById(R.id.website)
            val interest: EditText = findViewById(R.id.isInterested)

            // Fetch the candidate ID from intent extras (assuming you pass the ID from the previous activity)
            val candidateId = intent.getIntExtra("candidate_id", -1)

            // Access the CandidateDao
            val candidateDao = AppDatabase.getInstance(applicationContext).candidateDetailDao()
            Log.d("Candidate is", candidateId.toString())
            // Check if candidateId is valid
            if (candidateId != -1) {
                // Observe the LiveData returned by getCandidateById
                candidateDao.getCandidateById(candidateId).observe(this) { candidate ->
                    candidate?.let {
                        // Assign the fetched candidate data to the candidate variable
                        this.candidate = candidate

                        // Set EditText fields with candidate data
                        Name.setText(candidate.candidateName)
                        Spec.setText(candidate.candidateSpecial)
                        Details.setText(candidate.discussionDetails)
                        Email.setText(candidate.candidateEmail)
                        phone.setText(candidate.candidatePhone.toString())
                        website.setText(candidate.website)
                        interest.setText(candidate.isInterseted)
                    }
                }
            }

            // Set click listener for the button
            btn.setOnClickListener {
                // Retrieve the text from EditText fields
                val candidateName = Name.text.toString()
                val candidateSpec = Spec.text.toString()
                val candidateDetails = Details.text.toString()
                val candidateEmail = Email.text.toString()
                val candidatePhone = phone.text.toString()
                val candidateWebsite = website.text.toString()
                val candidateInterest = interest.text.toString()

                // Create a Candidate object with the retrieved data or use the fetched candidate data if available
                val emptyCandidate = Candidate(
                    candidateName = "",
                    candidateSpecial = "",
                    candidateEmail = "",
                    candidatePhone = 0,
                    website = "",
                    isInterseted = "",
                    discussionDetails = ""
                )

                val updatedCandidate = candidate?: emptyCandidate// Default values for new candidate

                // Update the candidate object with the retrieved data
                updatedCandidate.apply {
                    this.candidateName = candidateName
                    this.candidateSpecial = candidateSpec
                    this.discussionDetails = candidateDetails
                    this.candidateEmail = candidateEmail
                    this.candidatePhone = candidatePhone.toInt()
                    this.website = candidateWebsite
                    this.isInterseted = candidateInterest
                }

                // Launch a coroutine in IO context to insert/update the candidate details
                GlobalScope.launch(Dispatchers.IO) {
                    if (candidateId != -1) {
                        // Update the existing candidate if candidateId is valid
                        candidateDao.updateCandidate(updatedCandidate)
                    } else {
                        // Insert a new candidate if candidateId is not valid
                        candidateDao.insertCandidate(updatedCandidate)
                    }
                }
                finish()
            }
        }
    }