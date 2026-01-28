package com.example.todoappv2

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseConfigTest {

    @Test
    fun testFirebaseInitialization() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Initialize Firebase if not already initialized
        val firebaseApp = if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
        } else {
            FirebaseApp.getInstance()
        }

        assertNotNull("FirebaseApp should be initialized", firebaseApp)
        
        val auth = FirebaseAuth.getInstance()
        assertNotNull("FirebaseAuth should be accessible", auth)
    }
}
