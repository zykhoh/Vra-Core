package com.fyp.Service.Classifier;

import com.fyp.ApplicationProperties;
import com.fyp.Model.Recognition;
import com.fyp.Utils.GraphBuilder;
import com.fyp.Utils.IOUtil;
import com.fyp.Utils.ImageUtil;
import com.fyp.Utils.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ObjectDetector class to detect objects using pre-trained models with TensorFlow Java API.
 */
@Service
public class ObjectDetector {
    private final static Logger LOGGER = LoggerFactory.getLogger(ObjectDetector.class);
    private byte[] GRAPH_DEF;
    private List<String> LABELS;
    private ApplicationProperties applicationProperties;

    @Autowired
    public ObjectDetector(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        try {
            GRAPH_DEF = IOUtil.readAllBytesOrExit(applicationProperties.getGraph());
            LABELS = IOUtil.readAllLinesOrExit(applicationProperties.getLabel());
            LOGGER.info("doing just alright");
        } catch (ServiceException ex) {
            LOGGER.error("Failed to read applicationProperties");
        }
    }

    /**
     * Detect objects on the given image
     * @param imageLocation the location of the image
     * @return a map with location of the labeled image and recognitions
     */
    public Map<String, Object> detect(final String imageLocation) {
        byte[] image = IOUtil.readAllBytesOrExit(imageLocation);
        try (Tensor<Float> normalizedImage = normalizeImage(image)) {
            List<Recognition> recognitions = YOLOClassifier.getInstance().classifyImage(executeYOLOGraph(normalizedImage), LABELS);
            printToConsole(recognitions);
            String labeledFilePath = ImageUtil.getInstance(applicationProperties).labelImage(image, recognitions, IOUtil.getFileName(imageLocation), applicationProperties.getOutputDir());

            Map<String, Object> result = new HashMap();
            result.put("labeledFilePath", labeledFilePath);
            result.put("recognitions", recognitions);
            return result;
        }
    }

    public Map<String, Object> detect(final String imageLocation, final String outputDir) {
        byte[] image = IOUtil.readAllBytesOrExit(imageLocation);
        try (Tensor<Float> normalizedImage = normalizeImage(image)) {
            List<Recognition> recognitions = YOLOClassifier.getInstance().classifyImage(executeYOLOGraph(normalizedImage), LABELS);
            printToConsole(recognitions);
            ArrayList<String> recognitionList = new ArrayList<String>();

            for (Recognition recognition : recognitions) {
                recognitionList.add(recognition.getTitle());
            }

            String labeledFilePath = ImageUtil.getInstance(applicationProperties).labelImage(image, recognitions, IOUtil.getFileName(imageLocation), outputDir);

            Map<String, Object> result = new HashMap<>();
            result.put("labeledFilePath", labeledFilePath);
            result.put("recognitions", recognitionList);
            return result;
        }
    }

    /**
     * Pre-process input. It resize the image and normalize its pixels
     * @param imageBytes Input image
     * @return Tensor<Float> with shape [1][416][416][3]
     */
    private Tensor<Float> normalizeImage(final byte[] imageBytes) {
        try (Graph graph = new Graph()) {
            GraphBuilder graphBuilder = new GraphBuilder(graph);

            final Output<Float> output =
                    graphBuilder.div( // Divide each pixels with the MEAN
                            graphBuilder.resizeBilinear( // Resize using bilinear interpolation
                                    graphBuilder.expandDims( // Increase the output tensors dimension
                                            graphBuilder.cast( // Cast the output to Float
                                                    graphBuilder.decodeJpeg(
                                                            graphBuilder.constant("input", imageBytes), 3),
                                                    Float.class),
                                            graphBuilder.constant("make_batch", 0)),
                                    graphBuilder.constant("size", new int[]{applicationProperties.getImageSize(), applicationProperties.getImageSize()})),
                            graphBuilder.constant("scale", applicationProperties.getImageMean()));

            try (Session session = new Session(graph)) {
                return session.runner().fetch(output.op().name()).run().get(0).expect(Float.class);
            }
        }
    }

    /**
     * Executes graph on the given preprocessed image
     * @param image preprocessed image
     * @return output tensor returned by tensorFlow
     */
    private float[] executeYOLOGraph(final Tensor<Float> image) {
        try (Graph graph = new Graph()) {
            graph.importGraphDef(GRAPH_DEF);
            try (Session s = new Session(graph);
                 Tensor<Float> result = s.runner().feed("input", image).fetch("output").run().get(0).expect(Float.class)) {
                float[] outputTensor = new float[YOLOClassifier.getInstance().getOutputSizeByShape(result)];
                FloatBuffer floatBuffer = FloatBuffer.wrap(outputTensor);
                result.writeTo(floatBuffer);
                return outputTensor;
            }
        }
    }

    /**
     * Prints out the recognize objects and its confidence
     * @param recognitions list of recognitions
     */
    private void printToConsole(final List<Recognition> recognitions) {
        for (Recognition recognition : recognitions) {
            LOGGER.info("Object: {} - confidence: {}", recognition.getTitle(), recognition.getConfidence());
        }
    }
}
