package filters

object Kernels {
  val kernLaplacian = Kernel(Array(
    Array(-1.0, -1.0, -1.0),
    Array(-1.0, 8.0, -1.0),
    Array(-1.0, -1.0, -1.0)
  ), Kernel.IGNORE_EDGES)

  val kernBlur = Kernel(Array(
    Array(0.0625, 0.125, 0.0625),
    Array(0.125, 0.25, 0.125),
    Array(0.0625, 0.125, 0.0625)
  ), Kernel.IGNORE_EDGES)

  val kernLoG = Kernel(
    Array(
      Array(0,1,1,2,2,2,1,1,0),
      Array(1,2,4,5,5,5,4,2,1),
      Array(1,4,5,3,0,3,5,4,1),
      Array(2,5,3,-12,-24,-12,3,5,2),
      Array(2,5,0,-24,-40,-24,0,5,2),
      Array(2,5,3,-12,-24,-12,3,5,2),
      Array(1,4,5,3,0,3,5,4,1),
      Array(1,2,4,5,5,5,4,2,1),
      Array(0,1,1,2,2,2,1,1,0)
    )
  )

  val kernSobelH = Kernel(Array(
    Array(-1.0, 0.0, 1.0),
    Array(-2.0, 0.0, 2.0),
    Array(-1.0, 0.0, 1.0)
  ), Kernel.IGNORE_EDGES)

  val kernSobelH2 = Kernel(Array(
    Array(1.0, 0.0, -1.0),
    Array(2.0, 0.0, -2.0),
    Array(1.0, 0.0, -1.0)
  ), Kernel.IGNORE_EDGES)

  val kernSobelV = Kernel(Array(
    Array(1.0, 2.0, 1.0),
    Array(0.0, 0.0, 0.0),
    Array(-1.0, -2.0, -1.0)
  ), Kernel.IGNORE_EDGES)

  val kernSobelV2 = Kernel(Array(
    Array(-1.0, -2.0, -1.0),
    Array(0.0, 0.0, 0.0),
    Array(1.0, 2.0, 1.0)
  ), Kernel.IGNORE_EDGES)

  val kernEmboss = Kernel(Array(
    Array(-2.0, -1.0, 0.0),
    Array(-1.0, 1.0, 1.0),
    Array(0.0, 1.0, 2.0)
  ), Kernel.IGNORE_EDGES)

  val kernSharpen = Kernel(Array(
    Array(-1.0, -1.0, -1.0),
    Array(-1.0, 9.0, -1.0),
    Array(-1.0, -1.0, -1.0)
  ), Kernel.IGNORE_EDGES)


}
