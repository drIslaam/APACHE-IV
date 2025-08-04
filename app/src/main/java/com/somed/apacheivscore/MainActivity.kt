package com.somed.apacheivscore

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.somed.apacheivscore.databinding.ActivityMainBinding
import com.somed.apacheivscore.R
import com.bumptech.glide.Glide

// Add these imports
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val calculator = ApacheIVCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Load user profile
        currentUser?.let { user ->
            // Set user name
            binding.profileName.text = user.displayName ?: "Guest"
            
            // Load profile image
            user.photoUrl?.let { photoUrl ->
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_avatar)
                    .into(binding.profileImage)
            }
        }
        
        /*
        // Example logout button - add this to your layout if you want logout functionality
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        */

        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        // Patient type spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.patient_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPatientType.adapter = adapter
        }

        // GCS spinners
        ArrayAdapter.createFromResource(
            this,
            R.array.gcs_eyes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGcsEyes.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.gcs_verbal,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGcsVerbal.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.gcs_motor,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGcsMotor.adapter = adapter
        }

        // Admission origin spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.admission_origins,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAdmissionOrigin.adapter = adapter
        }
    }

    private fun setupListeners() {
        // Sedation checkbox
        binding.cbSedated.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutGcs.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

        // Patient type change listener
        binding.spinnerPatientType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateDiagnosisSpinners(position == 0) // position 0 is Medical
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // System spinner listener
        binding.spinnerSystem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateDiagnosisOptions()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Calculate button
        binding.btnCalculate.setOnClickListener {
            if (validateInputs()) {
                calculateScore()
            }
        }
    }

    private fun updateDiagnosisSpinners(isMedical: Boolean) {
        val systems = if (isMedical) calculator.medsys else calculator.chisys
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            systems
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerSystem.adapter = adapter
        binding.spinnerSystem.setSelection(0)
    }

    private fun updateDiagnosisOptions() {
        val isMedical = binding.spinnerPatientType.selectedItemPosition == 0
        val systemIndex = binding.spinnerSystem.selectedItemPosition

        val diagnoses = if (isMedical) {
            if (systemIndex < calculator.meddia.size) {
                calculator.meddia[systemIndex].toList()
            } else {
                emptyList()
            }
        } else {
            if (systemIndex < calculator.chidia.size) {
                calculator.chidia[systemIndex].toList()
            } else {
                emptyList()
            }
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            diagnoses
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerDiagnosis.adapter = adapter
        binding.spinnerDiagnosis.setSelection(0)

        // Show/hide thrombolysis for cardiovascular system
        binding.layoutThrombolysis.visibility = if (binding.spinnerSystem.selectedItem == "Cardiovascular") {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.etAge.text.isNullOrEmpty()) {
            showToast("Please enter age")
            return false
        }

        if (binding.spinnerSystem.selectedItemPosition == 0) {
            showToast("Please select a system")
            return false
        }

        if (binding.spinnerDiagnosis.selectedItemPosition == 0) {
            showToast("Please select a diagnosis")
            return false
        }

        // Add more validations as needed for other required fields
        return true
    }

    private fun calculateScore() {
        try {
            val patientData = ApacheIVCalculator.PatientData(
                age = binding.etAge.text.toString().toDouble(),
                isMedicalPatient = binding.spinnerPatientType.selectedItemPosition == 0,
                temperature = binding.etTemperature.text.toString().toDoubleOrNull() ?: 37.0,
                meanArterialPressure = binding.etMap.text.toString().toDoubleOrNull() ?: 70.0,
                heartRate = binding.etHeartRate.text.toString().toDoubleOrNull() ?: 80.0,
                respiratoryRate = binding.etRespiratoryRate.text.toString().toDoubleOrNull() ?: 15.0,
                isVentilated = binding.rgVentilation.checkedRadioButtonId == R.id.rbVentilatedYes,
                fio2 = binding.etFio2.text.toString().toDoubleOrNull() ?: 21.0,
                po2 = binding.etPo2.text.toString().toDoubleOrNull() ?: 90.0,
                pco2 = binding.etPco2.text.toString().toDoubleOrNull() ?: 40.0,
                arterialPh = binding.etPh.text.toString().toDoubleOrNull() ?: 7.4,
                sodium = binding.etSodium.text.toString().toDoubleOrNull() ?: 140.0,
                urineOutput = binding.etUrineOutput.text.toString().toDoubleOrNull() ?: 1000.0,
                creatinine = (binding.etCreatinine.text.toString().toDoubleOrNull() ?: 80.0) * 0.011312,
                urea = binding.etUrea.text.toString().toDoubleOrNull() ?: 4.0,
                bloodSugar = binding.etBloodSugar.text.toString().toDoubleOrNull() ?: 100.0,
                albumin = binding.etAlbumin.text.toString().toDoubleOrNull() ?: 40.0,
                bilirubin = (binding.etBilirubin.text.toString().toDoubleOrNull() ?: 10.0) / 17.1,
                hematocrit = binding.etHematocrit.text.toString().toDoubleOrNull() ?: 40.0,
                whiteBloodCells = binding.etWbc.text.toString().toDoubleOrNull() ?: 10.0,
                isSedated = binding.cbSedated.isChecked,
                gcsEyes = if (binding.cbSedated.isChecked) 4 else binding.spinnerGcsEyes.selectedItemPosition + 1,
                gcsVerbal = if (binding.cbSedated.isChecked) 5 else binding.spinnerGcsVerbal.selectedItemPosition + 1,
                gcsMotor = if (binding.cbSedated.isChecked) 6 else binding.spinnerGcsMotor.selectedItemPosition + 1,
                hasChronicRenalFailure = binding.cbCrf.isChecked,
                hasCirrhosis = binding.cbCirrhosis.isChecked,
                hasHepaticFailure = binding.cbHepaticFailure.isChecked,
                hasMetastaticCarcinoma = binding.cbMetastaticCarcinoma.isChecked,
                hasLymphoma = binding.cbLymphoma.isChecked,
                hasLeukemia = binding.cbLeukemia.isChecked,
                hasImmunosuppression = binding.cbImmunosuppression.isChecked,
                hasAids = binding.cbAids.isChecked,
                preIcuLosDays = binding.etPreIcuLos.text.toString().toDoubleOrNull() ?: 0.0,
                admissionOrigin = binding.spinnerAdmissionOrigin.selectedItemPosition,
                isReadmission = binding.rgReadmission.checkedRadioButtonId == R.id.rbReadmissionYes,
                isEmergencySurgery = binding.rgEmergencySurgery.checkedRadioButtonId == R.id.rbEmergencySurgeryYes,
                diagnosisSystem = binding.spinnerSystem.selectedItemPosition,
                diagnosis = binding.spinnerDiagnosis.selectedItemPosition,
                receivedThrombolysis = binding.rgThrombolysis.checkedRadioButtonId == R.id.rbThrombolysisYes
            )

            val result = calculator.calculateApacheIVScore(patientData)
            displayResults(result)
        } catch (e: Exception) {
            showToast("Error calculating score: ${e.message}")
        }
    }

    private fun displayResults(result: ApacheIVCalculator.ApacheIVResult) {
        binding.tvApacheScore.text = "APACHE IV Score: ${result.apacheScore}/286"
        binding.tvApsScore.text = "APS Score: ${result.apsScore}/239"
        binding.tvMortality.text = "Mortality Rate: ${"%.1f".format(result.mortalityRate)}%"
        binding.tvLos.text = "Estimated LOS: ${"%.1f".format(result.lengthOfStay)} days"
        binding.layoutResults.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}