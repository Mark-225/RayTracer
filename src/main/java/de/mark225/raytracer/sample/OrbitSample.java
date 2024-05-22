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
import de.mark225.raytracer.scenemodel.SimpleSceneModel;
import org.spongepowered.math.matrix.Matrix3d;
import org.spongepowered.math.vector.Vector3d;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OrbitSample {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Plane plane = new Plane(Vector3d.UP, 0, new MaterialProperties(0.1f, 0f, 1f, 0.01f, .3f, 1f, new Color(0, 50, 0)));
        Sphere sphere = new Sphere(new Vector3d(0, 2, 0), 2, new MaterialProperties(0.05f, 1f, .1f, .05f, 0.2f, 2, new Color(0, 0, 50)));
        Sphere smallSphere = new Sphere(new Vector3d(4, 1, 1.5), 1, new MaterialProperties(.3f, 0, 1f, .05f, .2f, 2, new Color(50, 0, 0)));
        Sphere yellowSphere = new Sphere(new Vector3d(-3.5, 1.5, -3), 1.5, new MaterialProperties(.3f, 0, 1f, .05f, .2f, 2, new Color(100, 100, 0)));
        LightSource lightSource = new LightSource(new Vector3d(5, 6, -2), new Color(100, 100, 100), new Color(50, 50, 50), new Color(10, 10, 10));
        Vector3d cameraPosition = new Vector3d(0, 4, -5);
        Camera camera = new Camera(cameraPosition, Vector3d.UP, new Vector3d(0, 3, -2.5), (float) Math.toRadians(45), (float) Math.toRadians(45));
        Scene scene = new Scene(camera, List.of(lightSource), new PhongLighting(new LightSource[]{lightSource}), new SimpleSceneModel(List.of(plane, sphere, smallSphere, yellowSphere)));
        Raytracer raytracer = new Raytracer(scene, new Color(0, 100, 220), 1000, 1000, 4);

        int totalFrames = 600;
        List<Raytracer> raytracers = createOrbit(raytracer, Vector3d.ZERO, totalFrames);
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        File parentDir = new File("frames");
        parentDir.mkdir();
        System.out.println(STR."Starting render of \{totalFrames} frames");
        List<Callable<Integer>> tasks = new ArrayList<>(totalFrames);
        for(int i = 0; i < totalFrames; i++) {
            Raytracer rt = raytracers.get(i);
            int idx = i;
            tasks.add(() -> {
                BufferedImage bi = rt.render();
                File file = new File(parentDir, STR."\{idx}.png");
                try {
                    ImageIO.write(bi, "png", file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(STR."Rendered image \{counter.addAndGet(1)} of \{totalFrames}");
                return 0;
            });
        }
        List<Future<Integer>> futures = executor.invokeAll(tasks);
        for(Future<Integer> future : futures) {
            future.get();
        }
        executor.shutdown();

    }

    private static List<Raytracer> createOrbit(Raytracer raytracer, Vector3d center, int frames){
        Scene scene = raytracer.getScene();
        Camera camera = scene.camera();
        double distanceToPlane = camera.planeLoc().sub(camera.position()).length();
        double phi = Math.toRadians(360d/(double) frames);
        double sin = Math.sin(phi);
        double cos = Math.cos(phi);
        Matrix3d rotationMatrix = Matrix3d.from(
                cos, 0 , sin,
                0, 1, 0,
                -sin, 0 , cos);
        Vector3d currentVector = camera.position().sub(center);
        List<Raytracer> raytracers = new ArrayList<>(frames);
        for(int i = 0; i < frames; i++){
            currentVector = rotationMatrix.transform(currentVector);
            Vector3d cameraLocation = center.add(currentVector);
            Vector3d planeLocation = cameraLocation.add(currentVector.negate().normalize().mul(distanceToPlane));
            Camera newCam = new Camera(cameraLocation, camera.up(), planeLocation, camera.hfov(), camera.vfov());
            Scene newScene = new Scene(newCam, scene.lightSources(), scene.lightModel(), scene.sceneModel());
            Raytracer newRT = new Raytracer(newScene, raytracer.getSkyColor(), raytracer.getResX(), raytracer.getResY(), raytracer.getMaxBounces());
            raytracers.add(newRT);
        }
        return raytracers;
    }
}
