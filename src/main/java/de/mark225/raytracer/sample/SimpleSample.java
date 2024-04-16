package de.mark225.raytracer.sample;

import de.mark225.raytracer.Raytracer;
import de.mark225.raytracer.lighting.LightSource;
import de.mark225.raytracer.lighting.PhongLighting;
import de.mark225.raytracer.objects.MaterialProperties;
import de.mark225.raytracer.objects.SceneObject;
import de.mark225.raytracer.objects.primitives.Plane;
import de.mark225.raytracer.objects.primitives.Sphere;
import de.mark225.raytracer.scene.Camera;
import de.mark225.raytracer.scene.Scene;
import org.spongepowered.math.vector.Vector3d;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimpleSample {

    public static void main(String[] args) throws IOException {
        Plane plane = new Plane(Vector3d.UP, 0, new MaterialProperties(0.1f, 0f, 1f, 0.01f, .3f, 1f, new Color(0, 50, 0)));
        Sphere sphere = new Sphere(new Vector3d(0, 2, 0), 2, new MaterialProperties(0.3f, 0f, 1f, .05f, 0.2f, 2, new Color(0, 0, 50)));
        Sphere smallSphere = new Sphere(new Vector3d(4, 1, 1.5), 1, new MaterialProperties(.3f, 0, 1f, .05f, .2f, 2, new Color(50, 0, 0)));
        Sphere yellowSphere = new Sphere(new Vector3d(-3.5, 1.5, -3), 1.5, new MaterialProperties(.3f, 0, 1f, .05f, .2f, 2, new Color(100, 100, 0)));
        LightSource lightSource = new LightSource(new Vector3d(5, 6, -2), new Color(100, 100, 100), new Color(50, 50, 50), new Color(10, 10, 10));
        Vector3d cameraPosition = new Vector3d(0, 4, -5);
        Camera camera = new Camera(cameraPosition, Vector3d.UP, new Vector3d(0, 3, -2.5), (float) Math.toRadians(45), (float) Math.toRadians(45));
        Scene scene = new Scene(camera, List.of(lightSource), new PhongLighting(new LightSource[]{lightSource}), new SceneObject[]{plane, sphere, smallSphere, yellowSphere});
        Raytracer raytracer = new Raytracer(scene, new Color(0, 100, 220), 1000, 1000, 10);
        BufferedImage bi = raytracer.render();
        File f = new File("output.png");
        ImageIO.write(bi, "png", f);
        System.out.println("Output saved to " + f.getAbsolutePath());
    }

}
