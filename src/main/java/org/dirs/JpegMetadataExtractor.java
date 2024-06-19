package org.dirs;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import java.io.File;
import java.io.IOException;

public class JpegMetadataExtractor {

    public static String readJpegDateTaken(final File file) throws IOException {

        final ImageMetadata metadata = Imaging.getMetadata(file);

        if (metadata instanceof JpegImageMetadata) {
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            return retrieveTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
        }
        throw new RuntimeException("No metadata could be read for " + file.getName());
    }

    private static String retrieveTagValue(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
        final TiffField field = jpegMetadata.findExifValueWithExactMatch(tagInfo);
        if (field == null) {
            throw new RuntimeException("No exif value found for tag " + tagInfo);
        } else {
            return field.getValueDescription();
        }
    }
}
