package objects

import filters.{StructuralElement, StructuralElements}

case class ImageTrackerOptions(
  alpha: Double = 200.0,
  beta: Double = 100.0,
  pValueLimit: Double = 0.05,
  thresholdValue: Int = 65,
  structuralElement: StructuralElement = StructuralElements.structCross,
  minSize: Int = 50
)