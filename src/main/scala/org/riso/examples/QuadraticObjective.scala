package org.riso.examples

import org.riso.numerical.LBFGS
import breeze.linalg.norm
import breeze.optimize.{LBFGS => BrzLBFGS}
import breeze.optimize.DiffFunction
import breeze.numerics.Inf
import breeze.linalg.DenseMatrix
import breeze.linalg.DenseVector

/**
 * Created by debasish83 on 12/13/14.
 */
object QuadraticObjective {

  def getGram(nGram: Int) : DenseMatrix[Double] = {
    val hrand = DenseMatrix.rand[Double](nGram, nGram)
    val hrandt = hrand.t
    val hposdef = hrandt * hrand
    hposdef.t + hposdef
  }

  def getObjAndGrad(H: DenseMatrix[Double],
                    q: DenseVector[Double],
                    x: DenseVector[Double]) : (Double, DenseVector[Double]) = {
    val obj = x.t*H*x*0.5 + q.dot(x)
    val grad = H*x + q
    (obj, grad)
  }

  def optimizeWithLBFGS(init: DenseVector[Double],
                        H: DenseMatrix[Double],
                        q: DenseVector[Double]) = {
    val lbfgs = new BrzLBFGS[DenseVector[Double]](-1, 7)
    val f = new DiffFunction[DenseVector[Double]] {
      def calculate(x: DenseVector[Double]) = {
        getObjAndGrad(H, q, x)
      }
    }
    val bfgsResult = lbfgs.minimize(f, init)
    bfgsResult
  }

  def main(args : Array[String]) {
    if(args.length < 1) {
      println(s"Usage: QuadraticObjective n iters")
      println(s"n: dimension of quadratic problem")
      println(s"iters: BFGS iterations, default infinity")
      return
    }

    val n = args(0).toInt
    val iters = if (args.length > 1) args(1).toInt else Inf

    val ndim = 10*n
    val msave = 7
    val nwork = ndim * (2 * msave + 1) + 2 * msave

    println(s"Generating random quadratic problem of dimension $n")

    val H = getGram(n)
    val q = DenseVector.rand[Double](n)

    val x = DenseVector.rand[Double](n)
    val g = DenseVector.fill[Double](n)(0.0)

    val diag = Array.fill[Double](n)(0.0)

    val w = DenseVector.fill[Double](nwork)(0.0)

    val m = 7

    val eps = 1e-5
    val xtol = 1e-16
    var icall = 0
    var iflag = Array.fill[Int](1)(0)

    val iprint = Array[Int](0, 0)
    val diagco = false
    var f = 0.0

    val refx = H \ q:*(-1.0)
    val refObjAndGrad = getObjAndGrad(H, q, refx)

    println(s"LU Solve objective ${refObjAndGrad._1}")

    val init = DenseVector.rand[Double](n)
    val bfgsResult = optimizeWithLBFGS(init, H, q)

    val brzDiff = norm(refx - bfgsResult, Inf)

    val brzObjAndGrad = getObjAndGrad(H, q, bfgsResult)

    println(s"BreezeLBFGS objective ${brzObjAndGrad._1}")

    do {
      f = (x.t * H * x) * 0.5 + q.dot(x)
      g := H * x + q

      try {
        LBFGS.lbfgs(n, m, x.data, f, g.data, diagco, diag, iprint, eps, xtol, iflag)
      }
      catch {
        case e: LBFGS.ExceptionWithIflag => {
          println("QuadraticObjective: lbfgs failed")
          println(e)
          return
        }
      }
      icall += 1
    } while(iflag(0) != 0 && icall <= iters)

    val risoDiff = norm(refx - x, Inf)

    println(s"Solution diff InfNorm risoBfgs $risoDiff breezeBfgs $brzDiff")
  }
}
