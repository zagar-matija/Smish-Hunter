package ferit.student.matijazagar.smishhunter

data class AnalysisResult(
    val data : Data
)
data class Data(
    val attributes: Attributes
)

data class Attributes(
    val last_analysis_stats: AnalysisStats,
    val reputation: Int,
    val total_votes: Votes,
    val url: String
)

data class AnalysisStats(
    val harmless: Int,
    val malicious: Int,
    val suspicious: Int,
    val timeout: Int,
    val undetected: Int
)

data class Votes(
    val harmless: Int,
    val malicious: Int
)