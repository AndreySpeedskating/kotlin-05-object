package domain.service

import WallService
import exception.PostNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import util.TestData

class PostNotFoundExceptionTest {

    private lateinit var wallService: WallService

    @BeforeEach
    fun setUp() {
        wallService = WallService()
    }

    @Test
    fun `createComment should throw PostNotFoundException when post does not exist`() {
        // Given - попытка добавить комментарий к несуществующему посту
        val nonExistentPostId = 999
        val comment = TestData.createSimpleComment(postId = nonExistentPostId)

        // When & Then - функция выкидывает исключение
        val exception = assertThrows(PostNotFoundException::class.java) {
            wallService.createComment(nonExistentPostId, comment)
        }

        assertEquals("Post with id 999 not found", exception.message)
        assertEquals(0, wallService.commentsCount())
    }

    @Test
    fun `createComment should throw PostNotFoundException with correct message`() {
        // Given
        val nonExistentPostId = 123
        val comment = TestData.createSimpleComment(postId = nonExistentPostId)

        // When
        val exception = assertThrows(PostNotFoundException::class.java) {
            wallService.createComment(nonExistentPostId, comment)
        }

        // Then
        assertTrue(exception.message!!.contains("123"))
        assertEquals("Post with id 123 not found", exception.message)
    }

    @Test
    fun `multiple createComment calls should handle exceptions independently`() {
        // Given
        val existingPost = wallService.add(TestData.createSimplePost("Existing Post"))
        val nonExistentPostId = 999

        // When - успешное создание комментария
        val validComment = wallService.createComment(
            existingPost.id,
            TestData.createSimpleComment(postId = existingPost.id, text = "Valid comment")
        )

        // And - попытка создать комментарий к несуществующему посту (должен выбросить исключение)
        assertThrows(PostNotFoundException::class.java) {
            wallService.createComment(nonExistentPostId, TestData.createSimpleComment(postId = nonExistentPostId))
        }

        // Then - проверяем, что только один комментарий добавлен и система в стабильном состоянии
        assertEquals(1, wallService.commentsCount())
        assertEquals(1, wallService.postsCount())
        assertEquals(validComment.id, wallService.findCommentById(validComment.id)?.id)

        // And - можно продолжать работать с существующими постами
        val anotherComment = wallService.createComment(
            existingPost.id,
            TestData.createSimpleComment(postId = existingPost.id, text = "Another valid comment")
        )
        assertEquals(2, wallService.commentsCount())
        assertEquals(2, anotherComment.id)
    }
}
