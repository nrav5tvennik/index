
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ImageController
{
    @FXML
    private ImageView imageView;
    private Image image;

    public ImageController(Image image) {
        this.image = image;
    }

    @FXML
    public void initialize() {
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
    }
}