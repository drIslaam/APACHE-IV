package com.somed.apacheivscore

import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

class ApacheIVCalculator {

    // Medical systems and diagnoses
    val medsys = arrayOf(
        "System", "Cardiovascular", "Respiratory", "Digestive", "Neurologic",
        "Metabolic", "Hematologic", "Genitourinary", "Sepsis", "Trauma", "Miscellaneous"
    )
    
    val chisys = arrayOf(
        "System", "Cardiovascular", "Respiratory", "Digestive", "Neurosurgery",
        "Genitourinary", "Trauma", "Miscellaneous"
    )

    // Medical diagnoses
    val meddia = arrayOf(
        arrayOf("Diagnosis"),
        arrayOf("Diagnosis","AMI Ant","AMI Inf/Lat","AMI Non-Q","AMI Other","Cardiac Arrest","Cardiogenic Shock","Cardiomyopathy","Congestive HF","Chest Pain, rule out MI","Hypertension","Hypovolemia","Hemorrhage","Aortic Aneuysm","Peripheral Vascular Disease","Rythm Disturbance","Cardiac Drug Toxicity","Unstable Angina","Other"),
        arrayOf("Diagnosis","Airway Obstruction","Asthma","Aspiration Pneumonia","Bacterial Pneumonia","Viral Pneumonia","Parasitic/Fungal Pneumonia","COPD","Pleural Effusion","Edema noncardiac","Embolism","Respiratory Arrest","Cancer (lung, ENT)","Restrictive Disease","Other"),
        arrayOf("Diagnosis","Upper Bleeding","Lower Bleeding","Variceal Bleeding","Inflammatory Disease","Neoplasm","Obstruction","Perforation","Vascular Insufficiency","Hepatic Failure","Intra/Retroperitoneal Bleeding","Pancreatitis","Other"),
        arrayOf("Diagnosis","Intracerebral Hemorrhage","Neoplasm","Infection","Neuromuscular Disease","Drug Overdose","Subdural/Epidural Hematoma","SAH, Aneurysm","Seizure","Stroke","Other"),
        arrayOf("Diagnosis","Acid-base/Electrolyte Disorder","Diabetic Ketoacidosis","Hyperosmolar Diabetic Coma","Other"),
        arrayOf("Diagnosis","Coagulopathy, Neutro-/Thrombocyto-/Pancytopenia","Other"),
        arrayOf("Diagnosis","Renal/Other"),
        arrayOf("Diagnosis","Cutaneous","Gastrointestinal","Pulmonary","Urinary","Other","Unknown"),
        arrayOf("Diagnosis","Head with Chest/Abdomen/Pelvis/Spine","Head with Face/Extremity","Head only","Head with multi-trauma","Chest and Spine","Spine only","Multitrauma (no Head)"),
        arrayOf("Diagnosis","General/Other")
    )

    // Medical diagnosis values
    val vmeddia = arrayOf(
        arrayOf("0"),
        arrayOf("0","0.085768512","-0.036016015","-0.057916835","0","-0.751470488","0.329989886","0.557005388","-0.219091549","-0.520128136","-0.067961418","-0.389881468","-0.381155309","1.310621507","0.844326529","-0.302546395","-0.882634505","-0.368981979","-0.132883369"),
        arrayOf("0","0.347148464","-0.542185912","0.860258427","1.306744601","1.156064757","1.619700546","-0.446092483","0.810093029","1.44275094","0.106834049","-0.053291846","-0.140474342","1.024903996","0.240490909"),
        arrayOf("0","0.021360405","0.185696594","-0.083385343","0.532982522","1.336831287","2.06861446","2.208699567","1.944060834","0.194945835","0.165265635","2.762688675","0.556588745"),
        arrayOf("0","0.86329482","0.974847187","1.16556532","3.92566043","-0.89006849","1.195315097","3.00860876","-0.330810671","0.313233793","0.357504381"),
        arrayOf("0","-0.382017407","-0.58421484","-0.081962609","-0.363969853"),
        arrayOf("0","0.399817416","-0.126173954"),
        arrayOf("0","-0.152233731"),
        arrayOf("0","1.56421957","1.245313673","1.926647999","0.453137242","0.649077107","0.402590257"),
        arrayOf("0","2.128908172","0.860033343","0.834081141","3.637560295","2.574268317","2.168142671","1.521187936"),
        arrayOf("0","-0.420843422")
    )

    val v2meddia = arrayOf(
        arrayOf("0"),
        arrayOf("0","0.102949765","-0.152525079","-0.270872458","0","0.416919141","0.239711427","0.059962444","-0.422587793","-1.122354572","-0.813921325","-0.622592285","-0.656757432","0.649149305","-0.502752032","-0.603060945","-0.690943246","-1.212730735","-0.369658509"),
        arrayOf("0","-0.977669154","-1.540678498","-0.37223658","-0.043365914","0.254374904","1.056187347","-0.398697453","0.189900524","-0.2416873","-0.051527401","-0.390631425","0.966313802","1.55529658","-0.202823617"),
        arrayOf("0","-0.551832894","-0.579471867","-0.527719358","-0.211771894","0.195130291","-0.369945003","-0.327171182","0.714879336","-0.119675791","-0.659544401","-0.513627563","-0.252586643"),
        arrayOf("0","0.945056155","0.018952727","-0.535783229","-0.550653067","-1.55261952","0.295093901","0.615950385","-0.942170992","0.519453035","-0.176828697"),
        arrayOf("0","-0.640575517","-1.775702142","-0.927156778","-0.986438209"),
        arrayOf("0","0.258172435","-0.342352862"),
        arrayOf("0","-0.54158014"),
        arrayOf("0","0.126440315","-0.13010935","-0.258766455","-0.732788506","-0.04233671","-0.093377498"),
        arrayOf("0","-0.372350315","-0.364128015","0.595869416","-0.067960926","-0.717432122","0.033769484","-0.678110274"),
        arrayOf("0","-0.667576408")
    )

    // Surgical diagnoses
    val chidia = arrayOf(
        arrayOf("Diagnosis"),
        arrayOf("Diagnosis","Heart Valve","CABG with double/redo Valve","CABG with Single Valve","Aortic Aneurysm, Elective","Aortic Aneurysm, Ruptured","Aortic Aneurysm, Dissection","Femoro-popeliteal Bypass","Aorto-iliac/-femoral Bypass","Peripheral Ischemia","Carotid","Other"),
        arrayOf("Diagnosis","Thoracotomy (Malignancy)","ENT Neoplasm","Thoracotomy (Lung biopy/Pleural Disease)","Thoracotomy (Infection)","Other"),
        arrayOf("Diagnosis","Malignancy","Bleeding","Fistula/Abcess","Cholecystitis/Cholangitis","GI Inflammation","Obstruction","Perforation","Ischemia","Liver Transplant","Other"),
        arrayOf("Diagnosis","Neoplasm (Craniotomy/Transphenoidal)","Intracranial Hemorrhage","SAH (Aneurysm/AVM)","Subdural/Epidural Hematoma","Spinal Cord Surgery","Other"),
        arrayOf("Diagnosis","Neoplasm (Renal/Bladder/Prostate)","Renal Transplant","Hysterectomy","Other"),
        arrayOf("Diagnosis","Head Only","Multitrauma with Head","Extremity","Multitrauma (no Head)"),
        arrayOf("Diagnosis","Amputation (nontraumatic)")
    )

    // Surgical diagnosis values
    val vchidia = arrayOf(
        arrayOf("0"),
        arrayOf("0","-2.076438218","-0.332456954","-1.358289429","0.773946463","2.77663888","1.110021457","-0.204188913","0.52392239","0.102293462","-0.14649332","-1.28358348"),
        arrayOf("0","0.199925455","-0.053892181","0.185974867","0.044451048","-0.359443815"),
        arrayOf("0","0.324865875","-0.045403893","0.719937797","-0.588512311","0.686936442","0.187304735","1.055374625","0.62069525","-0.848656603","0.217044719"),
        arrayOf("0","0.319065119","2.949869569","2.599592539","1.342344962","-0.251392367","0.551331225"),
        arrayOf("0","-0.423760975","-0.556650506","-0.162493249","-0.58901834"),
        arrayOf("0","2.102182643","3.20631279","-0.481004907","1.485430308"),
        arrayOf("0","-0.325703861")
    )

    val v2chidia = arrayOf(
        arrayOf("0"),
        arrayOf("0","-1.371763972","-0.155141644","-1.99434806","-0.760703396","0.204404736","-0.178456475","-0.786571016","-0.831194514","-0.504208244","-1.332642438","-0.59044574"),
        arrayOf("0","0.086933576","-1.152870135","0.405738008","-0.005937516","-0.249217228"),
        arrayOf("0","0.136282662","-0.329679773","-0.556661177","-0.593293673","-0.165585527","-0.189005132","-0.189960264","0.498328014","-1.370278559","-0.295894316"),
        arrayOf("0","-0.437737676","0.52671741","0.318905704","0.715682622","-0.628609547","0.003996339"),
        arrayOf("0","-1.397212695","-1.308449407","-0.795847883","-0.693574061"),
        arrayOf("0","1.088819324","0.357797735","-0.180386981","-0.377807998"),
        arrayOf("0","0.604910303")
    )

    data class ApacheIVResult(
        val apacheScore: Int,
        val apsScore: Int,
        val mortalityRate: Double,
        val lengthOfStay: Double
    )

    data class PatientData(
        // Basic info
        val age: Double,
        val isMedicalPatient: Boolean, // true for medical, false for surgical
        
        // Vital signs (worst in first 24h)
        val temperature: Double, // °C
        val meanArterialPressure: Double, // mmHg
        val heartRate: Double, // /min
        val respiratoryRate: Double, // /min
        
        // Respiratory
        val isVentilated: Boolean,
        val fio2: Double, // %
        val po2: Double, // mmHg
        val pco2: Double, // mmHg
        val arterialPh: Double,
        
        // Labs
        val sodium: Double, // mEq/L
        val urineOutput: Double, // mL/24h
        val creatinine: Double, // mg/dL
        val urea: Double, // mEq/L
        val bloodSugar: Double, // mg/dL
        val albumin: Double, // g/L
        val bilirubin: Double, // mg/dL
        val hematocrit: Double, // %
        val whiteBloodCells: Double, // x1000/mm3
        
        // Neurological
        val isSedated: Boolean,
        val gcsEyes: Int, // 1-4
        val gcsVerbal: Int, // 1-5
        val gcsMotor: Int, // 1-6
        
        // Chronic health conditions
        val hasChronicRenalFailure: Boolean,
        val hasCirrhosis: Boolean,
        val hasHepaticFailure: Boolean,
        val hasMetastaticCarcinoma: Boolean,
        val hasLymphoma: Boolean,
        val hasLeukemia: Boolean,
        val hasImmunosuppression: Boolean,
        val hasAids: Boolean,
        
        // Admission info
        val preIcuLosDays: Double, // days
        val admissionOrigin: Int, // 0=other, 1=floor, 2=OR/recovery, 3=other hospital
        val isReadmission: Boolean,
        val isEmergencySurgery: Boolean,
        
        // Diagnosis
        val diagnosisSystem: Int, // index of system
        val diagnosis: Int, // index of diagnosis
        val receivedThrombolysis: Boolean, // for AMI only
        
        // Optional parameters for AaDO2 calculation
        val respiratoryQuotient: Double = 0.8,
        val atmosphericPressure: Double = 760.0
    )

    fun calculateApacheIVScore(data: PatientData): ApacheIVResult {
        var score = 0
        
        // Temperature scoring
        score += when {
            data.temperature < 33 -> 20
            data.temperature < 33.5 -> 16
            data.temperature < 34 -> 13
            data.temperature < 35 -> 8
            data.temperature < 36 -> 2
            data.temperature < 40 -> 0
            else -> 4
        }

        // MAP scoring
        score += when {
            data.meanArterialPressure <= 39 -> 23
            data.meanArterialPressure < 60 -> 15
            data.meanArterialPressure < 70 -> 7
            data.meanArterialPressure < 80 -> 6
            data.meanArterialPressure < 100 -> 0
            data.meanArterialPressure < 120 -> 4
            data.meanArterialPressure < 130 -> 7
            data.meanArterialPressure < 140 -> 9
            else -> 10
        }

        // Heart rate scoring
        score += when {
            data.heartRate < 40 -> 8
            data.heartRate < 50 -> 5
            data.heartRate < 100 -> 0
            data.heartRate < 110 -> 1
            data.heartRate < 120 -> 5
            data.heartRate < 140 -> 7
            data.heartRate < 155 -> 13
            else -> 17
        }

        // Respiratory rate scoring
        score += when {
            data.respiratoryRate <= 5 -> 17
            data.respiratoryRate < 12 -> 8
            data.respiratoryRate < 14 -> 7
            data.respiratoryRate < 25 -> 0
            data.respiratoryRate < 35 -> 6
            data.respiratoryRate < 40 -> 9
            data.respiratoryRate < 50 -> 11
            else -> 18
        }

        // Respiratory scoring (ventilation and oxygenation)
        val aad = (data.fio2 / 100) * (data.atmosphericPressure - 47) - (data.pco2 / data.respiratoryQuotient) - data.po2
        
        if (data.isVentilated) {
            if (data.fio2 >= 50) {
                score += when {
                    aad < 100 -> 0
                    aad < 250 -> 7
                    aad < 350 -> 9
                    aad < 500 -> 11
                    else -> 14
                }
            } else {
                score += when {
                    data.po2 < 50 -> 15
                    data.po2 < 70 -> 5
                    data.po2 < 80 -> 2
                    else -> 0
                }
            }
        } else {
            score += when {
                data.po2 < 50 -> 15
                data.po2 < 70 -> 5
                data.po2 < 80 -> 2
                else -> 0
            }
        }

        // pH and pCO2 scoring
        score += when {
            data.arterialPh < 7.2 -> if (data.pco2 < 50) 12 else 4
            data.arterialPh < 7.3 -> when {
                data.pco2 < 30 -> 9
                data.pco2 < 40 -> 6
                data.pco2 < 50 -> 3
                else -> 2
            }
            data.arterialPh < 7.35 -> when {
                data.pco2 < 30 -> 9
                data.pco2 < 45 -> 0
                else -> 1
            }
            data.arterialPh < 7.45 -> when {
                data.pco2 < 30 -> 5
                data.pco2 < 45 -> 0
                else -> 1
            }
            data.arterialPh < 7.5 -> when {
                data.pco2 < 30 -> 5
                data.pco2 < 35 -> 0
                data.pco2 < 45 -> 2
                else -> 12
            }
            data.arterialPh < 7.6 -> if (data.pco2 < 40) 3 else 12
            else -> when {
                data.pco2 < 25 -> 0
                data.pco2 < 40 -> 3
                else -> 12
            }
        }

        // Sodium scoring
        score += when {
            data.sodium < 120 -> 3
            data.sodium < 135 -> 2
            data.sodium < 155 -> 0
            else -> 4
        }

        // Urine output scoring
        score += when {
            data.urineOutput < 400 -> 15
            data.urineOutput < 600 -> 8
            data.urineOutput < 900 -> 7
            data.urineOutput < 1500 -> 5
            data.urineOutput < 2000 -> 4
            data.urineOutput < 4000 -> 0
            else -> 1
        }

        // Creatinine scoring (different for chronic renal failure)
        if (data.hasChronicRenalFailure) {
            score += when {
                data.creatinine < 0.5 -> 3
                data.creatinine < 1.5 -> 0
                data.creatinine < 1.95 -> 4
                else -> 7
            }
        } else {
            if (data.urineOutput < 410 && data.creatinine >= 1.5) {
                score += 10
            } else {
                score += when {
                    data.creatinine < 0.5 -> 3
                    data.creatinine < 1.5 -> 0
                    data.creatinine < 1.95 -> 4
                    else -> 7
                }
            }
        }

        // Urea scoring
        score += when {
            data.urea < 17 -> 0
            data.urea < 20 -> 2
            data.urea < 40 -> 7
            data.urea < 80 -> 11
            else -> 12
        }

        // Blood sugar scoring
        score += when {
            data.bloodSugar < 40 -> 8
            data.bloodSugar < 60 -> 9
            data.bloodSugar < 200 -> 0
            data.bloodSugar < 350 -> 3
            else -> 5
        }

        // Albumin scoring
        score += when {
            data.albumin < 20 -> 11
            data.albumin < 25 -> 6
            data.albumin < 45 -> 0
            else -> 4
        }

        // Bilirubin scoring
        score += when {
            data.bilirubin < 2 -> 0
            data.bilirubin < 3 -> 5
            data.bilirubin < 5 -> 6
            data.bilirubin < 8 -> 8
            else -> 16
        }

        // Hematocrit scoring
        score += when {
            data.hematocrit < 41 -> 3
            data.hematocrit < 50 -> 0
            else -> 3
        }

        // WBC scoring
        score += when {
            data.whiteBloodCells < 1 -> 19
            data.whiteBloodCells < 3 -> 5
            data.whiteBloodCells < 20 -> 0
            data.whiteBloodCells < 25 -> 1
            else -> 5
        }

        // Neurological scoring (GCS)
        if (!data.isSedated) {
            val gcsTotal = data.gcsEyes + data.gcsVerbal + data.gcsMotor
            
            if (data.gcsEyes == 1) {
                // Eyes never open
                when {
                    data.gcsMotor == 6 || data.gcsMotor == 5 -> {
                        if (data.gcsVerbal == 1) score += 16
                    }
                    data.gcsMotor == 4 || data.gcsMotor == 3 -> {
                        when (data.gcsVerbal) {
                            3, 2 -> score += 24
                            1 -> score += 33
                        }
                    }
                    data.gcsMotor == 2 || data.gcsMotor == 1 -> {
                        when (data.gcsVerbal) {
                            3, 2 -> score += 29
                            1 -> score += 48
                        }
                    }
                }
            } else {
                // Eyes open sometimes
                when (data.gcsMotor) {
                    6 -> {
                        score += when (data.gcsVerbal) {
                            5 -> 0
                            4 -> 3
                            3, 2 -> 10
                            1 -> 15
                            else -> 0
                        }
                    }
                    5 -> {
                        score += when (data.gcsVerbal) {
                            5 -> 3
                            4 -> 8
                            3, 2 -> 13
                            1 -> 15
                            else -> 0
                        }
                    }
                    4, 3 -> {
                        score += when (data.gcsVerbal) {
                            5 -> 3
                            4 -> 13
                            3, 2, 1 -> 24
                            else -> 0
                        }
                    }
                    2, 1 -> {
                        score += when (data.gcsVerbal) {
                            5 -> 3
                            4 -> 13
                            3, 2, 1 -> 29
                            else -> 0
                        }
                    }
                }
            }
        }

        val apsScore = score // APS is the score before age and chronic health conditions

        // Age scoring
        score += when {
            data.age < 45 -> 0
            data.age < 60 -> 5
            data.age < 65 -> 11
            data.age < 70 -> 13
            data.age < 75 -> 16
            data.age < 85 -> 17
            else -> 24
        }

        // Chronic health condition scoring
        score += when {
            data.hasAids -> 23
            data.hasHepaticFailure -> 16
            data.hasLymphoma -> 13
            data.hasMetastaticCarcinoma -> 11
            data.hasLeukemia -> 10
            data.hasImmunosuppression -> 10
            data.hasCirrhosis -> 4
            else -> 0
        }

        // Calculate mortality probability using logistic regression
        var x = -5.950471952
        var y = 1.673887925

        // Age terms - FIXED
        val age1 = if (data.age - 27 > 0) (data.age - 27).toDouble().pow(3) else 0.0
        val age2 = if (data.age - 51 > 0) (data.age - 51).toDouble().pow(3) else 0.0
        val age3 = if (data.age - 64 > 0) (data.age - 64).toDouble().pow(3) else 0.0
        val age4 = if (data.age - 74 > 0) (data.age - 74).toDouble().pow(3) else 0.0
        val age5 = if (data.age - 86 > 0) (data.age - 86).toDouble().pow(3) else 0.0

        x += data.age * 0.024177455
        x += age1 * -0.00000438862
        x += age2 * 0.0000501422
        x += age3 * -0.000127787
        x += age4 * 0.000109606
        x += age5 * -0.0000275723

        y += data.age * 0.017603395
        y += age1 * -7.68259E-06
        y += age2 * 3.95667E-05
        y += age3 * -0.000166793
        y += age4 * 0.000228156
        y += age5 * -9.32478E-05

        // APS terms - FIXED
        val aps1 = if (apsScore - 10 > 0) (apsScore - 10).toDouble().pow(3) else 0.0
        val aps2 = if (apsScore - 22 > 0) (apsScore - 22).toDouble().pow(3) else 0.0
        val aps3 = if (apsScore - 32 > 0) (apsScore - 32).toDouble().pow(3) else 0.0
        val aps4 = if (apsScore - 48 > 0) (apsScore - 48).toDouble().pow(3) else 0.0
        val aps5 = if (apsScore - 89 > 0) (apsScore - 89).toDouble().pow(3) else 0.0

        x += apsScore * 0.055634916
        x += aps1 * 0.00000871852
        x += aps2 * -0.0000451101
        x += aps3 * 0.00005038
        x += aps4 * -0.0000131231
        x += aps5 * -8.65349E-07

        y += apsScore * 0.04442699
        y += aps1 * -5.83049E-05
        y += aps2 * 0.000297008
        y += aps3 * -0.000404434
        y += aps4 * 0.000189251
        y += aps5 * -2.35199E-05

        // Length of stay terms
        val los = sqrt(data.preIcuLosDays)
        val los1 = if (los - 0.121 > 0) (los - 0.121).pow(3) else 0.0
        val los2 = if (los - 0.423 > 0) (los - 0.423).pow(3) else 0.0
        val los3 = if (los - 0.794 > 0) (los - 0.794).pow(3) else 0.0
        val los4 = if (los - 2.806 > 0) (los - 2.806).pow(3) else 0.0

        x += los * -0.310487496
        x += los1 * 1.474672511
        x += los2 * -2.8618857
        x += los3 * 1.42165901
        x += los4 * -0.034445822

        y += los * 0.459823129
        y += los1 * 0.397791937
        y += los2 * -0.945210953
        y += los3 * 0.588651266
        y += los4 * -0.041232251

        // Ventilation mode
        if (data.isVentilated) {
            x += 0.271760036
            y += 1.835309541
        }

        // Oxygenation
        x += (data.po2 / (data.fio2 / 100)) * -0.000397068
        y += (data.po2 / (data.fio2 / 100)) * -0.004581842

        // Neurological status
        if (data.isSedated) {
            x += 0.785764316
            y += 1.789326613
        } else {
            x += (15 - (data.gcsEyes + data.gcsVerbal + data.gcsMotor)) * 0.039117532
            y += ((data.gcsEyes + data.gcsVerbal + data.gcsMotor) - 15) * 0.015182904
        }

        // Chronic health conditions
        when {
            data.hasAids -> {
                x += 0.958100516
                y += -0.10285942
            }
            data.hasHepaticFailure -> {
                x += 1.037379925
                y += -0.16012995
            }
            data.hasLymphoma -> {
                x += 0.743471748
                y += -0.28079854
            }
            data.hasMetastaticCarcinoma -> {
                x += 1.086423752
                y += -0.491932974
            }
            data.hasLeukemia -> {
                x += 0.969308299
                y += -0.803754341
            }
            data.hasImmunosuppression -> {
                x += 0.435581083
                y += -0.07438064
            }
            data.hasCirrhosis -> {
                x += 0.814665088
                y += 0.362658613
            }
        }

        // Admission origin
        when (data.admissionOrigin) {
            1 -> {
                x += 0.017149193
                y += 0.006529382
            }
            2 -> {
                x += -0.583828121
                y += -0.599591763
            }
            3 -> {
                x += 0.022106266
                y += 0.855505043
            }
        }

        // Emergency surgery
        if (data.isEmergencySurgery) {
            x += 0.249073458
            y += 1.040690632
        }

        // Readmission
        if (data.isReadmission) {
            y += 0.540368459
        }

        // Thrombolysis (for AMI)
        if (data.receivedThrombolysis) {
            x += -0.579874039
            y += 0.062385214
        }

        // Diagnosis-specific coefficients
        if (data.isMedicalPatient) {
            x += v2meddia[data.diagnosisSystem][data.diagnosis].toDouble()
            y += vmeddia[data.diagnosisSystem][data.diagnosis].toDouble()
        } else {
            x += v2chidia[data.diagnosisSystem][data.diagnosis].toDouble()
            y += vchidia[data.diagnosisSystem][data.diagnosis].toDouble()
        }

        // Calculate mortality probability
        val mortalityProbability = exp(x) / (1 + exp(x)) * 100
        val lengthOfStayEstimate = y

        return ApacheIVResult(
            apacheScore = score,
            apsScore = apsScore,
            mortalityRate = mortalityProbability,
            lengthOfStay = lengthOfStayEstimate
        )
    }
}