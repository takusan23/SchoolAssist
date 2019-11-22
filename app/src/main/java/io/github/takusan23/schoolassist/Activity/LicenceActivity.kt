package io.github.takusan23.schoolassist.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.takusan23.schoolassist.R
import kotlinx.android.synthetic.main.activity_licence.*

class LicenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licence)

        val text = """
            
material-components/material-components-android 
        
Apache License 2.0
A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
            
---------
            
journeyapps/zxing-android-embedded
            
Copyright (C) 2012-2018 ZXing authors, Journey Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
            
        """.trimIndent()

        activity_licence_textview.text = text

    }
}
