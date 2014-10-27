import java.awt.{Graphics2D, Polygon}

import util.{LightMap, Ray, Point}

class PointLight(a: Int, b: Int, brightness: Int) extends Point(a, b) {
    implicit def Tupel32Tupel2(t: (Double, Ray, Ray)) = (t._2, t._3)

    def renderShadow(lightMap: LightMap, box: Box, lightCount: Int) = {
        val rays = new Array[Ray](4)
        val vertices = box.getVertices
        for (i <- 0 to 3) {
            rays(i) = new Ray(new Point(this), vertices(i))
        }
        val (ray1, ray2) = getFurthest(rays)
        val vertex = getFarthest(vertices)
        lightMap.rasterize(ray1, vertex, ray2)(lightCount)
    }

    def getFurthest(ray: Ray, rays: Array[Ray]): (Ray, Ray) = {
        if (rays.length < 2) {
            return (ray, rays(0))
        }
        val furthest = getFurthest(ray, rays.drop(1))
        if ((ray angleTo rays(0)) > (furthest._1 angleTo furthest._2)) {
            return (ray, rays(0))
        }
        furthest
    }
    def getFurthest(rays: Array[Ray]): (Ray, Ray) = {
        if (rays.length < 2) {
            return (rays(0), rays(0))
        }
        val furthestThis = getFurthest(rays(0), rays.drop(1))
        val furthestAll  = getFurthest(rays.drop(1))
        if ((furthestThis._1 angleTo furthestThis._2) > (furthestAll._1 angleTo furthestAll._2)) {
            return furthestThis
        }
        furthestAll
    }

    def getFarthest(vertices: Array[Point]): Point = {
        if (vertices.length < 2) {
            return vertices(0)
        }
        val farthest = getFarthest(vertices.drop(1))
        if ((vertices(0) distance this) > (farthest distance this)) {
            return vertices(0)
        }
        farthest
    }
}
