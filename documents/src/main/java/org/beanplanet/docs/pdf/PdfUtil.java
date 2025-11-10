/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.docs.pdf;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;

/**
 * PDF Utility class.
 */
public class PdfUtil {
    /**
     * Converts an image to a single page PDF document. This method applies all default settings for source image and
     * resulting PDF document including: auto-rotate image (adjust known incoming orientation from, say, mobile
     * devices and others), keep image aspect, centre image and A4 portrait output PDF.
     *
     * @param source the source image, where Jpeg, Gif, Tiff and PNG are currently supported.
     * @param destination the destination where the rendered PDF will be produced.
     */
    public static void convertImageToPdf(final Resource source, final Resource destination) {
        try (final OutputStream docOs = destination.getOutputStream(); final PDDocument doc = new PDDocument()){
            PDPage page = new PDPage(PDRectangle.A4);

            Resource sourceOrientated = correctExifImageOrientation(source);
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, sourceOrientated.readFullyAsBytes(), null);

            PDPageContentStream contents = new PDPageContentStream(doc, page);
            PDRectangle mediaBox = page.getMediaBox();

            float scaleWidth = mediaBox.getWidth() / pdImage.getWidth();
            float scaleHeight = mediaBox.getHeight() / pdImage.getHeight();
            float scale = (scaleWidth < 1f || scaleHeight < 1f) ? Math.min(scaleWidth, scaleHeight) : 1f;
            float startX = (mediaBox.getWidth() - (pdImage.getWidth() * scale)) / 2;
            float startY = (mediaBox.getHeight() - (pdImage.getHeight() * scale)) / 2;
            contents.drawImage(pdImage, startX, startY, pdImage.getWidth() * scale, pdImage.getHeight() * scale);

            contents.close();

            doc.addPage(page);
            doc.save(docOs);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    private static Resource correctExifImageOrientation(final Resource source) {
        try {
            final byte[] imageBytes = source.readFullyAsBytes();
            final ImageMetadata metadata = Imaging.getMetadata(imageBytes);
            final ImageInfo imageInfo = Imaging.getImageInfo(imageBytes);

            TiffImageMetadata tiffImageMetadata = null;
            if ( metadata instanceof JpegImageMetadata ) {
                tiffImageMetadata = ((JpegImageMetadata)metadata).getExif();
            } else if ( metadata instanceof TiffImageMetadata ) {
                tiffImageMetadata = (TiffImageMetadata) metadata;
            } else {
                return source;
            }

            final TiffField orientationTag = tiffImageMetadata.findField(TiffTagConstants.TIFF_TAG_ORIENTATION);
            if ( orientationTag == null) return source;

            return renderOrientated(source, imageInfo, imageBytes, orientationTag.getIntValue());
        } catch (IOException | ImageReadException | ImageWriteException ex) {
            throw new IoException(ex);
        }
    }

    private static Resource renderOrientated(Resource source, ImageInfo info, byte[] imageBytes, int orientation) throws IOException, ImageReadException, ImageWriteException {
        AffineTransform t = new AffineTransform();
        switch (orientation) {
            case 0:
            case 1:  // No orientation
                return source;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.getWidth(), 0);
                break;
            case 3: // PI rotation
                t.translate(info.getWidth(), info.getHeight());
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.getHeight());
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(info.getHeight(), 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-info.getHeight(), 0);
                t.translate(0, info.getWidth());
                t.rotate(  3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, info.getWidth());
                t.rotate(  3 * Math.PI / 2);
                break;
        }

        AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BICUBIC);

        final BufferedImage sourceImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage destinationImage = op.filter(sourceImage, null);
        return new ByteArrayResource(Imaging.writeImageToBytes(destinationImage, ImageFormats.PNG, null));
    }

    public static byte[] extractPdfPageToPNGBytes(final Resource source, int pageNum) {
        try (final InputStream docIs = source.getInputStream(); PDDocument doc = PDDocument.load(docIs)) {
            PDFRenderer renderer = new PDFRenderer(doc);
            return Imaging.writeImageToBytes(renderer.renderImage(pageNum), ImageFormats.PNG, null);
        } catch (IOException | ImageWriteException ex) {
            throw new IoException(ex);
        }
    }
}
