package javalabs.libraries;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


/**
 * Load image, provide rectangle for rubberband selection. Press right mouse button for "crop" context menu which then crops the image at the selection rectangle and saves it as jpg.
 */
public class CropImage {

    RubberBandSelection rubberBandSelection;
    ImageView imageView;
    Stage cropStage;

    //@Override
    public CropImage(Image image) {
        this.cropStage = new Stage();
        cropStage.setTitle("Подрезка фотографии");
        BorderPane root = new BorderPane();
        // Контейнер для изображения
        ScrollPane scrollPane = new ScrollPane();
        // Слой изображений (картинка и выделение)
        Group imageLayer = new Group();
        // Ширина картинки
        double w = image.getWidth();
        // Высота картинки
        double h = image.getHeight();
        // Проверка на корректный размер изображения. Не даем загружать слишком маленькие картинки
        if(w < 200 || h < 200){
            Alert alert = new Alert(null,
                    "Фотография слишком маленькая. Выберите фото большего размера",
                    ButtonType.OK);
            alert.getDialogPane().setMinHeight(150);
            Optional<ButtonType> result = alert.showAndWait();
            cropStage.close();
        }
        // Преобразование в ImageView
        imageView = new ImageView(image);
        // Добавляем изображение на слой
        imageLayer.getChildren().add(imageView);
        // Устанавливаем в родительский контейнер
        scrollPane.setContent(imageLayer);
        // Добавляем родительский контейнер в сцену
        root.setCenter(scrollPane);
        // Выбранная область
        rubberBandSelection = new RubberBandSelection(imageLayer);

        // Добавляем выбор действия в контекстное меню
        ContextMenu contextMenu = new ContextMenu();

        MenuItem cropMenuItem = new MenuItem("Обрезать");
        // Обработчик для контекстного меню
        cropMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // Выделенная область
                Bounds selectionBounds = rubberBandSelection.getBounds();
                // Отладочный лог
                System.out.println( "Selected area: " + selectionBounds);
                // Обрезка
                crop(selectionBounds);

            }
        });
        // Добавляем пункт в контекстное меню
        contextMenu.getItems().add(cropMenuItem);

        // set context menu on image layer
        imageLayer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            }
        });
        // Минимальный размер окна
        w = w > 800 ? 800 : w;
        h = h > 800 ? 800 : h;
        cropStage.setScene(new Scene(root, w, h));
        cropStage.showAndWait();
    }

    private void crop(Bounds bounds) {

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        if(width < 200){
            Alert alert = new Alert(null,
                    "Выделенная область слишком мала! Необходимо выбрать область изображения большего размера (Выделенная область должна окраситься в голубой цвет)",
                    ButtonType.OK);
            alert.getDialogPane().setMinHeight(150);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufImageRGB, "jpeg", stream);
            BufferedImage newImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImage.createGraphics();
            g.drawImage(bufImageRGB, 0, 0, 200, 200, null);
            g.dispose();
            Image res = SwingFXUtils.toFXImage(newImage, null );
            imageView = new ImageView(res);
            cropStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();

    }

    /**
     * Обработка перетаскивания области выделения
     */
    public static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;


        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // Удаление устаревшей области
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);


                // Нулевые координаты выделяемой области
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add(rect);

            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                if( offsetX > 0){
                    if(offsetX > 200){
                        rect.setStroke(Color.BLUE);
                    } else rect.setStroke(Color.RED);
                    rect.setWidth(offsetX);
                    rect.setHeight(offsetX);
                } else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                    rect.setHeight(dragContext.mouseAnchorX - rect.getX());
                }
            }
        };


        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;
            }
        };
        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }

    public Image getImageView(){
        return imageView.getImage();
    }
}
