import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WallServiceTest {
    private val service = WallService()

    @BeforeEach
    fun setUp() {
        service.clear()
    }

    @Test
    fun `add should assign incremental id`() {
        val post = createSimplePost("First post")
        val result = service.add(post)

        assertEquals(1, result.id)
        assertEquals("First post", result.text)
    }

    @Test
    fun `update should return true when post exists`() {
        val post = service.add(createSimplePost("Original"))
        val updated = post.copy(text = "Updated")

        assertTrue(service.update(updated))
        assertEquals("Updated", service.findById(post.id)?.text)
    }

    @Test
    fun `update should return false when post not found`() {
        val post = createSimplePost("Test").copy(id = 999)

        assertFalse(service.update(post))
    }

    @Test
    fun `post with attachments should store them correctly`() {
        val photo = Photo(1, 123, listOf(PhotoSize("url", 100, 100, "m")))
        val attachment = PhotoAttachment(photo)
        val post = createSimplePost("With photo").copy(attachments = listOf(attachment))

        val result = service.add(post)

        assertEquals(1, result.attachments.size)
        assertEquals("photo", result.attachments.first().type)
    }

    @Test
    fun `post with nullable fields should handle them properly`() {
        val post = createSimplePost("Test").copy(
            signerId = null,
            createdBy = null
        )

        val result = service.add(post)

        assertNull(result.signerId)
        assertNull(result.createdBy)
        assertEquals("Test", result.text)
    }

    private fun createSimplePost(text: String) = Post(
        text = text,
        ownerId = 123,
        fromId = 123
    )
}