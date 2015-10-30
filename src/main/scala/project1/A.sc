List(1,2,3).splitAt(1)

List(
  List(1,2,3),
  List(4,5,6),
  List(7,8,9)
).transpose.transpose


//val f = "att-normalize.csv".toFile
//val l = "attLabel.csv".toFile
//
//(f.lines zip l.lines).foreach {
//  case (attrs, label) =>
//    "att-normalize-with-label.csv".toFile.createIfNotExists() << s"$label,$attrs"
//}