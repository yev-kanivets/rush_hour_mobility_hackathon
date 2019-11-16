package fr.nodesigner.meaoo.mqtt.androidsample.activity

import android.app.Activity
import android.os.Bundle
import fr.nodesigner.meaoo.androidsample.R
import kotlinx.android.synthetic.main.mission_activity.buttonGo

class MissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mission_activity)

        buttonGo.setOnClickListener { setResult(RESULT_OK); finish() }
    }
}
