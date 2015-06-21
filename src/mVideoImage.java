import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class mVideoImage extends JPanel {

    private final ImagePane mOnscreenPicture;

    public mVideoImage() {
        super();
        mOnscreenPicture = new ImagePane();
        this.add(mOnscreenPicture);
        this.setVisible(true);
        
    }

    public void setImage(final BufferedImage aImage) {
        mOnscreenPicture.setImage(aImage);
    }

    //....Inner class here....//

    public class ImagePane extends JPanel {

        private BufferedImage mImage;

        public void setImage(BufferedImage image) {
            SwingUtilities.invokeLater(new ImageRunnable(image));
        }

        @Override
        public Dimension getPreferredSize() {
            return mImage == null ? new Dimension(613, 806) : new Dimension(mImage.getWidth(), mImage.getHeight());
        }

        private class ImageRunnable implements Runnable {

            private final BufferedImage newImage;

            public ImageRunnable(BufferedImage newImage) {
                super();
                this.newImage = newImage;
            }

            @Override
            public void run() {
                ImagePane.this.mImage = newImage;
                Dimension size = getPreferredSize();
                final Dimension newSize = new Dimension(mImage.getWidth(), mImage.getHeight());
                if (!newSize.equals(size)) {
                    mVideoImage.this.invalidate();
                    mVideoImage.this.revalidate();
                    repaint();
                }
                repaint();
            }
        }

        public ImagePane() {
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (mImage != null) {
                g.drawImage(mImage, 0, 0, this);
            }
        }
    }
}