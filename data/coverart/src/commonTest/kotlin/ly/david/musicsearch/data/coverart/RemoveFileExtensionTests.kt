package ly.david.musicsearch.data.coverart

import org.junit.Assert.assertEquals
import org.junit.Test

class RemoveFileExtensionTests {

    @Test
    fun png() {
        assertEquals(
            "https://ia902903.us.archive.org/6/items/mbid-86351876-1cb2-489c-a85d-65026f6249e0/mbid-86351876-1cb2-489c-a85d-65026f6249e0-29781447652",
            "https://ia902903.us.archive.org/6/items/mbid-86351876-1cb2-489c-a85d-65026f6249e0/mbid-86351876-1cb2-489c-a85d-65026f6249e0-29781447652.png".removeFileExtension(),
        )
    }

    @Test
    fun jpg() {
        assertEquals(
            "https://ia601209.us.archive.org/16/items/mbid-10eef06f-b74a-4db8-a775-dc2107a038c0/mbid-10eef06f-b74a-4db8-a775-dc2107a038c0-29781622068_thumb500",
            "https://ia601209.us.archive.org/16/items/mbid-10eef06f-b74a-4db8-a775-dc2107a038c0/mbid-10eef06f-b74a-4db8-a775-dc2107a038c0-29781622068_thumb500.jpg".removeFileExtension(),
        )
    }

    @Test
    fun noFileExtension() {
        assertEquals(
            "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
            "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228".removeFileExtension(),
        )
    }
}
