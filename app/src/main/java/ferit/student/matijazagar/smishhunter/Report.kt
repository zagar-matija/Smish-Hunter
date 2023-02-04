package ferit.student.matijazagar.smishhunter

data class Report(
    var sender : String?,
    var content : String,
    var rating : String,
    var explanation : String
)
//TODO add parameter of AnalysisReport for diplay in reportsFragment