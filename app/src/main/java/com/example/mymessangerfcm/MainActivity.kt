package com.example.mymessangerfcm

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.example.core.domain.logic.core.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {

        private const val RC_SIGN_IN = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        singIn()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun singIn() {
        val user = FirebaseAuth.getInstance().currentUser
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
        if (user!=null) {
            // User is signed in.

            Log.d("Nurs","if true $user")
        } else {
            Log.d("Nurs","if false $user")
            // No user is signed in.
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val progressDialog = indeterminateProgressDialog("Setting up your account")
                initCurrentUserIfFirstTime {
//                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                    progressDialog.dismiss()
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}

fun Context.indeterminateProgressDialog(
    message: CharSequence? = null,
    title: CharSequence? = null,
    init: (ProgressDialog.() -> Unit)? = null
) = progressDialog(true, message, title, init)


private fun Context.progressDialog(
    indeterminate: Boolean,
    message: CharSequence? = null,
    title: CharSequence? = null,
    init: (ProgressDialog.() -> Unit)? = null
) = ProgressDialog(this).apply {
    isIndeterminate = indeterminate
    if (!indeterminate) setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    if (message != null) setMessage(message)
    if (title != null) setTitle(title)
    if (init != null) init()
    show()
}

private val currentUserDocRef: DocumentReference
    get() = FirebaseFirestore.getInstance().document("users/${FirebaseAuth.getInstance().currentUser?.uid
        ?: throw NullPointerException("UID is null.")}")

fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
    currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
        if (!documentSnapshot.exists()) {
            val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                "", null)
            currentUserDocRef.set(newUser).addOnSuccessListener {
                onComplete()
            }
        } else
            onComplete()
    }
}