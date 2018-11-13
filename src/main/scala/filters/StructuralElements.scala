package filters

object StructuralElements {
  val structCross = new StructuralElement(Vector(
    (0,0),
    (1,0),
    (-1,0),
    (0,1),
    (0,-1)
  ))

  val structSquare = new StructuralElement(Vector(
    (0,0),
    (1,0),
    (-1,0),
    (0,1),
    (0,-1),
    (1,1),
    (-1,1),
    (-1,-1),
    (1,-1)
  ))
}
