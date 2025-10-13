package domain.service

import WallService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.TestData

class PostWithNullableFieldsTest {

    private val wallService = WallService()

    @Test
    fun `post with all nullable fields as null should be handled correctly`() {
        // Given
        val post = TestData.createPostWithNullableFields(
            text = "Test post",
            signerId = null,
            createdBy = null
        )

        // When
        val result = wallService.add(post)

        // Then
        assertNull(result.signerId)
        assertNull(result.createdBy)
        assertEquals("Test post", result.text)
    }

    @Test
    fun `post with some nullable fields set should be handled correctly`() {
        // Given
        val post = TestData.createPostWithNullableFields(
            text = "Test post",
            signerId = 123,
            createdBy = null
        )

        // When
        val result = wallService.add(post)

        // Then
        assertEquals(123, result.signerId)
        assertNull(result.createdBy)
    }

    @Test
    fun `update should handle nullable fields correctly`() {
        // Given
        val original = wallService.add(
            TestData.createPostWithNullableFields(
                text = "Original",
                signerId = 100,
                createdBy = 200
            )
        )

        // When
        val updated = original.copy(
            text = "Updated",
            signerId = null,
            createdBy = 300
        )
        wallService.update(updated)

        // Then
        val found = wallService.findById(original.id)
        assertNotNull(found)
        assertEquals("Updated", found?.text)
        assertNull(found?.signerId)
        assertEquals(300, found?.createdBy)
    }
}