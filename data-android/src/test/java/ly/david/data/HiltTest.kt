package ly.david.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
abstract class HiltTest {
    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule by lazy { HiltAndroidRule(this) }
}
