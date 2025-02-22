import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withAnnotationOf
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import org.junit.jupiter.api.Test

class KonsistTest {

    // https://docs.konsist.lemonappdev.com/inspiration/snippets/general-snippets

    @Test
    fun `companion object is last declaration in the class`() {
        Konsist
            .scopeFromProject()
            .classes()
            .assertTrue {
                val companionObject = it.objects(includeNested = false).lastOrNull { obj ->
                    obj.hasModifier(KoModifier.COMPANION)
                }

                if (companionObject != null) {
                    it.declarations(
                        includeNested = false,
                        includeLocal = false,
                    ).last() == companionObject
                } else {
                    true
                }
            }
    }

    @Test
    fun `no class should use Java util logging`() {
        Konsist
            .scopeFromProject()
            .files
            .assertFalse { it.hasImport { import -> import.name == "java.util.logging.." } }
    }

    @Test
    fun `package name must match file path`() {
        Konsist
            .scopeFromProject()
            .packages
            .assertTrue { it.hasMatchingPath }
    }

    @Test
    fun `no wildcard imports allowed`() {
        Konsist
            .scopeFromProject()
            .imports
            .assertFalse { it.isWildcard }
    }

    // https://docs.konsist.lemonappdev.com/inspiration/snippets/android-snippets

    @Test
    fun `classes with 'UseCase' suffix should reside in 'domain' and 'usecase' package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.resideInPackage("..domain..") }
    }

    @Test
    fun `no class should use Android util logging`() {
        Konsist
            .scopeFromProject()
            .files
            .assertFalse { it.hasImport { import -> import.name == "android.util.Log" } }
    }

    @Test
    fun `All JetPack Compose previews contain 'Preview' in method name`() {
        Konsist
            .scopeFromProject()
            .functions()
            .withAnnotationOf(
                Preview::class,
                PreviewLightDark::class,
                DefaultPreviews::class,
            )
            .assertTrue {
                it.hasNameContaining("Preview")
            }
    }

    // https://docs.konsist.lemonappdev.com/inspiration/snippets/test-snippets

    @Test
    fun `classes with 'Test' Annotation should have 'Test' suffix`() {
        Konsist
            .scopeFromSourceSet(
                "test",
                "androidUnitTest",
            )
            .classes()
            .filter {
                it.functions().any { func -> func.hasAnnotationOf(Test::class) } ||
                    it.functions().any { func -> func.hasAnnotationWithName("org.junit.Test") }
            }
            .assertTrue { it.hasNameEndingWith("Test") }
    }

    @Test
    fun `clean architecture layers have correct dependencies`() {
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                val domain = Layer("domain", "ly.david.musicsearch.shared.domain..")
                val data = Layer("data", "ly.david.musicsearch.data..")
                val feature = Layer("feature", "ly.david.musicsearch.shared.feature..")
                domain.dependsOnNothing()
                data.dependsOn(domain)
                feature.dependsOn(domain)
                data.doesNotDependOn(feature)
                // failing
//                feature.doesNotDependOn(data)
            }
    }

//    @Test
//    fun `no class should use JUnit4 Test annotation`() {
//        Konsist
//            .scopeFromProject()
//            .classes()
//            .functions()
//            .assertFalse {
//                it.annotations.any { annotation ->
//                    annotation.fullyQualifiedName == "org.junit.Test"
//                }
//            }
//    }

//    @Test
//    fun `files reside in package that is derived from module name`() {
//        Konsist.scopeFromProduction()
//            .files
//            .assertTrue {
//                val featurePackageName = it.moduleName
//                it.hasPackage("ly.david.musicsearch.$featurePackageName..")
//            }
//    }
}
