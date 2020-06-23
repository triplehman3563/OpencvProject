package Ming.PureOpencv;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFX extends Application {
	static Mat2Image mat2Img = new Mat2Image();
	static Scalar lower;
	static Scalar upper;
	static Image imageOrigin ;
	static {
		nu.pattern.OpenCV.loadShared();

	}

	// 是否開始截取畫面
	private boolean isStart = false;

	// 截取畫面物件
	private VideoCapture capture;
	// 截取畫面控制物件
	private ScheduledExecutorService timer;

	// JavaFX 元件
	private BorderPane root;

	private VBox vboxCenter,vboxBottom;
	private ImageView frame,frame2;
	private HBox hboxBottom1,hboxBottom2,hboxBottom3,hboxCenter1;
	private Button videoButton, exitButton,setUpperBtn,setLowerBtn;
	private TextField txtUpper1,txtUpper2,txtUpper3,txtLower1,txtLower2,txtLower3;

	@Override
	public void start(Stage primaryStage) {
		lower = new Scalar(0,10,60);
		upper = new Scalar(180,30,100);
		// 建立 JavaFX 畫面
		initGui();

		// 建立截取畫面物件
		capture = new VideoCapture();

		// 結束按鈕事件
		exitButton.setOnAction((ActionEvent event) -> {
			System.exit(0);
		});

		videoButton.setOnAction((ActionEvent event) -> {

			// 啟動
			if (!isStart) {
				// 設定顯示截取畫面元件的寬度
				frame.setFitWidth(800);
				frame.setFitHeight(600);
				frame.setPreserveRatio(true);
				frame2.setFitWidth(800);
				frame2.setFitHeight(600);
				frame2.setPreserveRatio(true);


				// 開啟設備與設定解析度
				capture.open(0);
				capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 800);
				capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 600);

				if (capture.isOpened()) {
					isStart = true;

					// 截取畫面執行緒
					Runnable frameGrabber = new Runnable() {

						@Override
						public void run() {
							// 讀取與設定畫面
							Image imageToShow = grabFrame();

							frame.setImage(imageToShow);

							frame2.setImage(imageOrigin);
						}
					};

					// 建立與啟動截取畫面執行緒
					timer = Executors.newSingleThreadScheduledExecutor();
					// 設定 33ms 為截取畫面間隔時間, 一秒 30 frames
					timer.scheduleAtFixedRate(frameGrabber, 
							0, 33, TimeUnit.MILLISECONDS);

					videoButton.setText("Stop");
				}
				else {
					System.err.println("Open camera error!");
				}
			}
			// 停止
			else {
				isStart = false; 
				videoButton.setText("Start");

				try {
					timer.shutdown();
					timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				} 
				catch (InterruptedException e) {
					System.err.println(e);
				}

				// 清除
				capture.release();
				frame.setImage(null);
			}
		});

		setUpperBtn.setOnAction((ActionEvent event) -> {
			double upper1,upper2,upper3;
			upper1 = Double.parseDouble(txtUpper1.getText());
			upper2 = Double.parseDouble(txtUpper2.getText());
			upper3 = Double.parseDouble(txtUpper3.getText());
			upper = new Scalar(upper1,upper2,upper3); 
			System.out.println(upper);

		});
		setLowerBtn.setOnAction((ActionEvent event) -> {

			double lower1,lower2,lower3;
			lower1 = Double.parseDouble(txtLower1.getText());
			lower2 = Double.parseDouble(txtLower2.getText());
			lower3 = Double.parseDouble(txtLower3.getText());
			lower = new Scalar(lower1,lower2,lower3); 
			System.out.println(lower);

		});





		Scene scene = new Scene(root, 1600, 900);
		primaryStage.setTitle("Pixel Demo 01");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// 讀取畫面並傳回 Image 物件
	private Image grabFrame() {
		Image result = null;
		Mat image = new Mat();

		if (capture.isOpened()) {
			capture.read(image);

			if (!image.empty()) {


				MatProcess(image);
				result = mat2Image(".png", image);
			}
		}

		return result;
	}    

	// 轉換 Mat 為 Image 物件
	//  String ext  格式, 例如 .png 
	//  Mat image   影像物件
	public static Image mat2Image(String ext, Mat image) {
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(ext, image, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}    
	public static Mat MatProcess (Mat image) {
		final Size ksize = new Size(11, 11);
		Core.flip(image, image, 1);
		imageOrigin = mat2Image(".png", image);
		Imgproc.GaussianBlur(image, image, ksize, 0);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);

		Core.inRange(image,lower,upper,image);
		int erosion_size = 5;
		int dilation_size = 5;
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size+1));
		Imgproc.erode(image, image, element);
		Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*dilation_size + 1, 2*dilation_size+1));
		Imgproc.dilate(image, image, element1);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
		Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		for(int i=0; i< contours.size();i++)
	    {
	        if (Imgproc.contourArea(contours.get(i)) > 50 )
	        {
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            if ((rect.height > 35 && rect.height < 60) && (rect.width > 35 && rect.width < 60))
	            {
	                //Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
	            }
	        }
	    }

		return null;

	}

	// 建立 JavaFX 畫面
	private void initGui() {
		root = new BorderPane();
		root.setPadding(new Insets(5, 5, 5, 5));


		vboxCenter = new VBox();
		vboxCenter.setAlignment(Pos.CENTER);
		vboxCenter.setPadding(new Insets(5, 5, 5, 5));
		frame = new ImageView();
		frame2 = new ImageView();

		hboxCenter1 = new HBox();

		vboxCenter.getChildren().addAll(hboxCenter1);
		hboxCenter1.setAlignment(Pos.CENTER);
		hboxCenter1.setPadding(new Insets(5, 5, 5, 5));
		hboxCenter1.getChildren().addAll(frame, frame2);

		root.setCenter(vboxCenter);



		vboxBottom = new VBox();
		vboxBottom.setAlignment(Pos.CENTER);
		vboxCenter.setPadding(new Insets(5, 5, 5, 5));



		hboxBottom1 = new HBox();
		hboxBottom2 = new HBox();
		hboxBottom3 = new HBox();
		vboxBottom.getChildren().addAll(hboxBottom1,hboxBottom2,hboxBottom3);
		root.setBottom(vboxBottom);

		hboxBottom1.setAlignment(Pos.CENTER);
		hboxBottom1.setPadding(new Insets(5, 5, 5, 5));
		videoButton = new Button("Start");
		exitButton = new Button("Exit");
		hboxBottom1.getChildren().addAll(videoButton, exitButton);


		//root.setCenter(hboxBottom1);


		hboxBottom2.setAlignment(Pos.CENTER);
		hboxBottom2.setPadding(new Insets(5, 5, 5, 5));
		txtUpper1 = new TextField();
		txtUpper2 = new TextField();
		txtUpper3 = new TextField();
		setUpperBtn = new Button("SetUpper");
		hboxBottom2.getChildren().addAll(txtUpper1, txtUpper2,txtUpper3,setUpperBtn);

		hboxBottom3.setAlignment(Pos.CENTER);
		hboxBottom3.setPadding(new Insets(5, 5, 5, 5));
		txtLower1 = new TextField();
		txtLower2 = new TextField();
		txtLower3 = new TextField();
		setLowerBtn = new Button("SetLower");
		hboxBottom3.getChildren().addAll(txtLower1,txtLower2,txtLower3,setLowerBtn);
		//root.setBottom(hboxBottom2);
	}

	public static void main(String[] args) {
		launch(args);
	}

}