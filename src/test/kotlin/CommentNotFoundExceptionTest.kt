package domain.service

import WallService
import exception.CommentNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import util.TestData

class CommentNotFoundExceptionTest {

    private lateinit var wallService: WallService

    @BeforeEach
    fun setUp() {
        wallService = WallService()
    }

    @Test
    fun `reportComment should throw CommentNotFoundException when comment does not exist`() {
        // Given
        val nonExistentCommentId = 999

        // When & Then
        val exception = assertThrows(CommentNotFoundException::class.java) {
            wallService.reportComment(nonExistentCommentId, Reason.SPAM)
        }

        assertEquals("Comment with id 999 not found", exception.message)
        assertEquals(0, wallService.reportsCount())
    }

    @Test
    fun `reportComment should throw CommentNotFoundException with correct message`() {
        // Given
        val nonExistentCommentId = 456

        // When
        val exception = assertThrows(CommentNotFoundException::class.java) {
            wallService.reportComment(nonExistentCommentId, Reason.INSULT)
        }

        // Then
        assertTrue(exception.message!!.contains("456"))
        assertEquals("Comment with id 456 not found", exception.message)
    }

    @Test
    fun `reportComment should work correctly when comment exists`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test Post"))
        val comment = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))

        // When
        val report = wallService.reportComment(comment.id, Reason.SPAM)

        // Then
        assertEquals(1, report.id)
        assertEquals(comment.id, report.commentId)
        assertEquals(Reason.SPAM, report.reason)
        assertEquals(1, wallService.reportsCount())
    }

    @Test
    fun `multiple report operations should handle CommentNotFoundException correctly`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test Post"))
        val comment = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))

        // When - успешная жалоба
        val report = wallService.reportComment(comment.id, Reason.SPAM)

        // And - попытка пожаловаться на несуществующий комментарий
        assertThrows(CommentNotFoundException::class.java) {
            wallService.reportComment(999, Reason.SPAM)
        }

        // Then - проверяем, что только одна жалоба добавлена
        assertEquals(1, wallService.reportsCount())
        assertEquals(comment.id, report.commentId)

        // And - можно продолжать работать с существующими комментариями
        val anotherReport = wallService.reportComment(comment.id, Reason.VIOLENCE)
        assertEquals(2, wallService.reportsCount())
        assertEquals(2, anotherReport.id)
    }
}