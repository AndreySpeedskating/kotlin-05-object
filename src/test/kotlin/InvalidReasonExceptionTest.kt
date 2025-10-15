package domain.service

import WallService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import util.TestData

class InvalidReasonExceptionTest {

    private lateinit var wallService: WallService

    @BeforeEach
    fun setUp() {
        wallService = WallService()
    }

    @Test
    fun `reportComment should accept all valid reasons`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test Post"))
        val comment = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))

        // When & Then - все стандартные причины должны работать
        assertDoesNotThrow {
            Reason.values().forEach { reason ->
                wallService.reportComment(comment.id, reason)
            }
        }

        // Проверяем, что все жалобы созданы
        assertEquals(Reason.values().size, wallService.reportsCount())
    }

    @Test
    fun `reportComment should work with different reasons for same comment`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test Post"))
        val comment = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))

        // When
        val spamReport = wallService.reportComment(comment.id, Reason.SPAM)
        val insultReport = wallService.reportComment(comment.id, Reason.INSULT)
        val violenceReport = wallService.reportComment(comment.id, Reason.VIOLENCE)

        // Then
        assertEquals(3, wallService.reportsCount())
        assertEquals(Reason.SPAM, spamReport.reason)
        assertEquals(Reason.INSULT, insultReport.reason)
        assertEquals(Reason.VIOLENCE, violenceReport.reason)
        assertEquals(comment.id, spamReport.commentId)
        assertEquals(comment.id, insultReport.commentId)
        assertEquals(comment.id, violenceReport.commentId)
    }

    @Test
    fun `getReportsByReason should return correct reports`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test Post"))
        val comment1 = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))
        val comment2 = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))

        wallService.reportComment(comment1.id, Reason.SPAM)
        wallService.reportComment(comment1.id, Reason.INSULT)
        wallService.reportComment(comment2.id, Reason.SPAM)
        wallService.reportComment(comment2.id, Reason.VIOLENCE)

        // When
        val spamReports = wallService.getReportsByReason(Reason.SPAM)
        val insultReports = wallService.getReportsByReason(Reason.INSULT)
        val violenceReports = wallService.getReportsByReason(Reason.VIOLENCE)

        // Then
        assertEquals(2, spamReports.size)
        assertEquals(1, insultReports.size)
        assertEquals(1, violenceReports.size)
        assertTrue(spamReports.all { it.reason == Reason.SPAM })
        assertTrue(insultReports.all { it.reason == Reason.INSULT })
    }

    @Test
    fun `getReportsForComment should return correct reports`() {
        // Given
        val post = wallService.add(TestData.createSimplePost("Test Post"))
        val comment1 = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))
        val comment2 = wallService.createComment(post.id, TestData.createSimpleComment(postId = post.id))

        wallService.reportComment(comment1.id, Reason.SPAM)
        wallService.reportComment(comment1.id, Reason.INSULT)
        wallService.reportComment(comment2.id, Reason.VIOLENCE)

        // When
        val comment1Reports = wallService.getReportsForComment(comment1.id)
        val comment2Reports = wallService.getReportsForComment(comment2.id)

        // Then
        assertEquals(2, comment1Reports.size)
        assertEquals(1, comment2Reports.size)
        assertTrue(comment1Reports.all { it.commentId == comment1.id })
        assertTrue(comment2Reports.all { it.commentId == comment2.id })
    }
}