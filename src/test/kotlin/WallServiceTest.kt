package domain.service

import WallService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import util.TestData

class WallServiceTest {

    private lateinit var wallService: WallService

    @BeforeEach
    fun setUp() {
        wallService = WallService()
    }

    @Test
    fun `add should assign incremental id to post`() {
        // Given
        val post = TestData.createSimplePost("First post")

        // When
        val result = wallService.add(post)

        // Then
        assertEquals(1, result.id)
        assertEquals("First post", result.text)
        assertEquals(1, wallService.postsCount())
    }

    @Test
    fun `add should throw exception when post has non-zero id`() {
        // Given
        val post = TestData.createSimplePost("Test").copy(id = 5)

        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            wallService.add(post)
        }
    }

    @Test
    fun `update should return true and update post when post exists`() {
        // Given
        val original = wallService.add(TestData.createSimplePost("Original"))
        val updated = original.copy(text = "Updated text")

        // When
        val result = wallService.update(updated)

        // Then
        assertTrue(result)
        assertEquals("Updated text", wallService.findById(original.id)?.text)
    }

    @Test
    fun `update should return false when post does not exist`() {
        // Given
        val nonExistingPost = TestData.createSimplePost("Test").copy(id = 999)

        // When
        val result = wallService.update(nonExistingPost)

        // Then
        assertFalse(result)
    }

    @Test
    fun `findById should return post when exists`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test"))

        // When
        val found = wallService.findById(post.id)

        // Then
        assertNotNull(found)
        assertEquals(post.id, found?.id)
        assertEquals("Test", found?.text)
    }

    @Test
    fun `findById should return null when post does not exist`() {
        // When
        val found = wallService.findById(999)

        // Then
        assertNull(found)
    }

    @Test
    fun `findById and findPostById should be equivalent`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test"))

        // When
        val foundById = wallService.findById(post.id)
        val foundPostById = wallService.findPostById(post.id)

        // Then
        assertEquals(foundById, foundPostById)
        assertEquals(post.id, foundById?.id)
        assertEquals(post.id, foundPostById?.id)
    }

    @Test
    fun `getAllPosts should return all posts`() {
        // Given
        val post1 = wallService.add(TestData.createSimplePost("First"))
        val post2 = wallService.add(TestData.createSimplePost("Second"))

        // When
        val allPosts = wallService.getAllPosts()

        // Then
        assertEquals(2, allPosts.size)
        assertTrue(allPosts.any { it.id == post1.id })
        assertTrue(allPosts.any { it.id == post2.id })
    }

    @Test
    fun `clear should remove all posts and reset counter`() {
        // Given
        wallService.add(TestData.createSimplePost("First"))
        wallService.add(TestData.createSimplePost("Second"))

        // When
        wallService.clear()

        // Then
        assertEquals(0, wallService.postsCount())
        assertEquals(1, wallService.add(TestData.createSimplePost("New")).id)
    }

    @Test
    fun `post with attachments should store them correctly`() {
        // Given
        val photoAttachment = TestData.createPhotoAttachment()
        val post = TestData.createPostWithAttachments(photoAttachment)

        // When
        val result = wallService.add(post)

        // Then
        assertEquals(1, result.attachments.size)
        assertEquals("photo", result.attachments.first().type)
    }
}