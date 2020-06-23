package Ming.PureOpencv;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import javax.swing.JTextField;
import javax.swing.JDesktopPane;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;

public class App extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane1,contentPane2;
	static VideoCapture cap;
	static Mat mat;
	static Mat2Image mat2Img = new Mat2Image();
	static BufferedImage Image;
	public static JPanel panel;
	private JTextField textF_lowerBound_0;
	private JTextField textF_lowerBound_1;
	private JTextField textF_lowerBound_2;
	private JTextField textF_upperBound_0;
	private JTextField textF_upperBound_1;
	private JTextField textF_upperBound_2;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		nu.pattern.OpenCV.loadShared();

		//System.out.println(""+mat2Img.getImage(mat2Img.mat));
		try {
			App frame = new App();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (true) {
			cap = new VideoCapture();
			cap.open(0);
			cap.read(mat2Img.mat);//Mat
			//panel.setBounds(0, 0, mat2Img.img.getWidth(), mat2Img.img.getHeight());
			//<process Image> 
			final Size ksize = new Size(11, 11);
			Imgproc.GaussianBlur(mat2Img.mat, mat2Img.mat, ksize, 0);
			Imgproc.cvtColor(mat2Img.mat, mat2Img.mat, Imgproc.COLOR_BGR2HSV);
			Scalar lower = new Scalar(0,0,0);
			Scalar upper = new Scalar(0,0,50);
			Core.inRange(mat2Img.mat,lower,upper,mat2Img.mat);
//			Imgproc.erode(mat2Img.mat, mat2Img.mat, null, null, 2);
//			Imgproc.dilate(mat2Img.mat, mat2Img.mat, null, null, 2);
//			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//			List<MatOfPoint2f> contours2f    = new ArrayList<MatOfPoint2f>();
//			MatOfPoint max_contour = new MatOfPoint();
//			Imgproc.findContours(mat2Img.mat,contours,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
//			if(contours.size()>0)
//			{
//				double maxArea = 0;
//				int index = 0;
//				for(MatOfPoint element : contours) 
//				{
//					index++;
//					double area = Imgproc.contourArea(element);
//					if(area > maxArea){
//						maxArea = area;
//						max_contour = element;
//
//					}
//
//
//				}
//				//
//				contours2f.get(0).convertTo(max_contour, CvType.CV_32S);
//				Point[] centers      = new Point[contours.size()];
//				float[] radius     = new float[contours.size()];
//				Imgproc.minEnclosingCircle( contours2f.get(0), centers[0], radius );
//				//
//
//			}


			//</process Image> 

			Image = mat2Img.getImage(mat2Img.mat);//  BufferedImage
			//


		}

	}

	public App() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1280, 720);
		contentPane1 = new JPanel();
		contentPane1.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane1);
		 contentPane1.setLayout(null);
		
		 panel = new JPanel();
		 panel.setBounds(0, 0, 800, 600);
		 contentPane1.add(panel);
		 panel.setLayout(new BorderLayout(0, 0));
		 
		 textF_lowerBound_0 = new JTextField();
		 textF_lowerBound_0.setBounds(829, 10, 96,23);
		 contentPane1.add(textF_lowerBound_0);
		 textF_lowerBound_0.setColumns(10);
		 
		 textF_lowerBound_1 = new JTextField();
		 textF_lowerBound_1.setBounds(935, 11, 96, 21);
		 contentPane1.add(textF_lowerBound_1);
		 textF_lowerBound_1.setColumns(10);
		 
		 textF_lowerBound_2 = new JTextField();
		 textF_lowerBound_2.setBounds(1044, 11, 96, 21);
		 contentPane1.add(textF_lowerBound_2);
		 textF_lowerBound_2.setColumns(10);
		 
		
		 
		 textF_upperBound_0 = new JTextField();
		 textF_upperBound_0.setBounds(829, 43, 96, 21);
		 contentPane1.add(textF_upperBound_0);
		 textF_upperBound_0.setColumns(10);
		 
		 textF_upperBound_1 = new JTextField();
		 textF_upperBound_1.setBounds(935, 43, 96, 21);
		 contentPane1.add(textF_upperBound_1);
		 textF_upperBound_1.setColumns(10);
		 
		 textF_upperBound_2 = new JTextField();
		 textF_upperBound_2.setBounds(1044, 43, 96, 21);
		 contentPane1.add(textF_upperBound_2);
		 textF_upperBound_2.setColumns(10);
		 
		 JButton btn_setLowerBound = new JButton("set lower");
		 btn_setLowerBound.setBounds(1167, 10, 87, 23);
		 contentPane1.add(btn_setLowerBound);
		 
		 
		 JButton btn_setUpperBound = new JButton("set upper");
		 btn_setUpperBound.setBounds(1167, 43, 87, 23);
		 contentPane1.add(btn_setUpperBound);
		//		contentPane2 = new JPanel();
		//		contentPane2.setBorder(new EmptyBorder(0, 640, 0, 1280));
		//		setContentPane(contentPane2);
		//		contentPane2.setLayout(null);
		

		new MyThread().start();
	}
	public void paint(Graphics g){
		g = panel.getGraphics();
		//h = desktopPane.getGraphics();
		g.drawImage(Image, 0, 0, this);
		//h.drawImage(Image, 0, 0, this);
	}
	class MyThread extends Thread{
		@Override
		public void run() {
			 
			for (;;){
				repaint();
				try { Thread.sleep(30);//  set FPS?
				} catch (InterruptedException e) {    }
			}
		}
	}
}
