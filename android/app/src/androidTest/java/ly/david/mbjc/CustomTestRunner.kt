package ly.david.mbjc

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner

// A custom runner to set up the instrumented application class for tests.
// It shows as unused but it's used in our build.gradle file.
class CustomTestRunner : AndroidJUnitRunner() {

    // https://github.com/square/okhttp/issues/3184#issuecomment-462210825
    override fun onCreate(arguments: Bundle) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}
