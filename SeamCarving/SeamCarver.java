import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

public class SeamCarver {
    private BufferedImage input;
    private BufferedImage carved;

    public SeamCarver(String path) throws IOException {
        this.carved = null;
        File input_file = new File(path);
        this.input = ImageIO.read(input_file);
        carved = copyImage(input);
    }

    public void carveImage(int width) {
        int num = width;
        int tmp = num;
        if (num >= input.getWidth() || num < 0) {
            System.out.println("wrong size");
            System.exit(0);
        }
        while (num > 0) {
            Seam seam;
            getProgress(tmp, num);
            double[][] table = getTable(carved);
            seam = getSeam(table);
            remove(seam);
            num--;
        }
    }

    private double[][] getTable(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        double[][] table = new double[w][h];
        int[] value = new int[w * h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int left, right, total;
                int xPrev = image.getRGB((x - 1 + w) % w, y);
                int xNext = image.getRGB((x + 1 + w) % w, y);
                if (x == 0) {
                    left = value[y * w + (w - 1)];
                    right = value[y * w + x + 1];
                } else if (x == w - 1) {
                    left = value[y * w + x - 1];
                    right = value[y * w];
                } else {
                    left = value[y * w + x - 1];
                    right = value[y * w + x + 1];
                }
                left = getEnergy(xPrev, xNext);
                int yPrev = image.getRGB(x, (y - 1 + h) % h);
                int yNext = image.getRGB(x, (y + 1 + h) % h);
                right = getEnergy(yPrev, yNext);
                total = left + right;
                table[x][y] = total;
            }
        }
        return table;
    }

    private int getEnergy(int rgb1, int rgb2) {
        double b1 = (rgb1) & 0xff;
        double g1 = (rgb1 >> 8) & 0xff;
        double r1 = (rgb1 >> 16) & 0xff;
        double b2 = (rgb2) & 0xff;
        double g2 = (rgb2 >> 8) & 0xff;
        double r2 = (rgb2 >> 16) & 0xff;
        int energy = (int) ((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2));
        return energy;
    }

    private Seam getSeam(double[][] table) {
        int w = table.length;
        int h = table[0].length;
        Seam seam = new Seam(h);
        double[][] dynamic = new double[w][h];
        int[][] prev = new int[w][h];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                double min_value;
                if (j == 0) {
                    dynamic[i][j] = table[i][j];
                    prev[i][j] = -1;
                    continue;
                } else if (i == 0) {
                    min_value = Math.min(dynamic[i][j - 1], dynamic[i + 1][j - 1]);
                    if (min_value == dynamic[i][j - 1]) {
                        prev[i][j] = i;
                    } else {
                        prev[i][j] = i + 1;
                    }
                } else if (i == w - 1) {
                    min_value = Math.min(dynamic[i][j - 1], dynamic[i - 1][j - 1]);
                    if (min_value == dynamic[i][j - 1]) {
                        prev[i][j] = i;
                    } else {
                        prev[i][j] = i - 1;
                    }
                } else {
                    min_value = Math.min(dynamic[i][j - 1], Math.min(dynamic[i - 1][j - 1], dynamic[i + 1][j - 1]));
                    if (min_value == dynamic[i][j - 1]) {
                        prev[i][j] = i;
                    } else if (min_value == dynamic[i - 1][j - 1]) {
                        prev[i][j] = i - 1;
                    } else {
                        prev[i][j] = i + 1;
                    }
                }
                dynamic[i][j] = table[i][j] + min_value;
            }
        }
        double min_energy = dynamic[0][h - 1];
        int min_coord = 0;
        for (int i = 0; i < w; i++) {
            if (min_energy > dynamic[i][h - 1]) {
                min_energy = dynamic[i][h - 1];
                min_coord = i;
            }
        }
        seam.setEnergy(min_energy);
        for (int j = h - 1; j >= 0; j--) {
            seam.setPixels(j, min_coord);
            min_coord = prev[min_coord][j];
        }
        return seam;
    }

    private void remove(Seam seam) {
        int w = carved.getWidth();
        int h = carved.getHeight();
        BufferedImage img;
        img = new BufferedImage(w - 1, h, BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < h; j++) {
            boolean next = false;
            for (int i = 0; i < w - 1; i++) {
                if (seam.getPixels()[j] == i) {
                    next = true;
                }
                if (next) {
                    img.setRGB(i, j, carved.getRGB(i + 1, j));
                } else {
                    img.setRGB(i, j, carved.getRGB(i, j));
                }
            }
        }
        carved = img;
    }

    public void saveImage(String file_path, String type) throws IOException {
        File output_file = new File(file_path);
        ImageIO.write(carved, type, output_file);
        System.out.println("Done.");
    }

    private BufferedImage copyImage(BufferedImage img) {
        ColorModel color_model = img.getColorModel();
        boolean isAlphaPremultiplied = color_model.isAlphaPremultiplied();
        WritableRaster writable_raster = img.copyData(null);
        return new BufferedImage(color_model, writable_raster, isAlphaPremultiplied, null);

    }

    private void getProgress(int total, int current) {
        double t = total;
        double c = current;
        String percentage = String.format("%.1f", (t - c) / t * 100);
        System.out.print(percentage + "%\r");
    }

    static class Seam {
        private double energy;
        private int[] pixels;
        private int size;

        public Seam(int s) {
            this.size = s;
            pixels = new int[s];
        }

        int[] getPixels() {
            return pixels;
        }

        void setPixels(int position, int value) {
            pixels[position] = value;
        }

        double getEnergy() {
            return energy;
        }

        void setEnergy(double energy) {
            this.energy = energy;
        }

        int getSize() {
            return size;
        }
    }
}