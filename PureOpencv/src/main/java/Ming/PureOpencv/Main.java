package Ming.PureOpencv;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import Ming.PureOpencv.App.MyThread;
import javax.swing.JTextField;

public class Main extends JFrame {
	private JPanel contentPane1,contentPane2;
	static VideoCapture cap;
	static Mat mat;
	static Mat2Image mat2Img = new Mat2Image();
	static BufferedImage Image;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JPanel panel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		nu.pattern.OpenCV.loadShared();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				while (true) {
					cap = new VideoCapture();
					cap.open(0);
					cap.read(mat2Img.mat);//Mat

					//<process Image> 
					final Size ksize = new Size(11, 11);
					Imgproc.GaussianBlur(mat2Img.mat, mat2Img.mat, ksize, 0);
					Imgproc.cvtColor(mat2Img.mat, mat2Img.mat, Imgproc.COLOR_BGR2HSV);
					Scalar lower = new Scalar(10,100,20);
					Scalar upper = new Scalar(25,255,255);
					Core.inRange(mat2Img.mat,lower,upper,mat2Img.mat);
//					Imgproc.erode(mat2Img.mat, mat2Img.mat, null, null, 2);
//					Imgproc.dilate(mat2Img.mat, mat2Img.mat, null, null, 2);
//					List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//					List<MatOfPoint2f> contours2f    = new ArrayList<MatOfPoint2f>();
//					MatOfPoint max_contour = new MatOfPoint();
//					Imgproc.findContours(mat2Img.mat,contours,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
//					if(contours.size()>0)
//					{
//						double maxArea = 0;
//						int index = 0;
//						for(MatOfPoint element : contours) 
//						{
//							index++;
//							double area = Imgproc.contourArea(element);
//							if(area > maxArea){
//								maxArea = area;
//								max_contour = element;
		//
//							}
		//
		//
//						}
//						//
//						contours2f.get(0).convertTo(max_contour, CvType.CV_32S);
//						Point[] centers      = new Point[contours.size()];
//						float[] radius     = new float[contours.size()];
//						Imgproc.minEnclosingCircle( contours2f.get(0), centers[0], radius );
//						//
		//
//					}


					//</process Image> 

					Image = mat2Img.getImage(mat2Img.mat);//  BufferedImage
					//


				}
				
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1280, 720);
		contentPane1 = new JPanel();
		contentPane1.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane1);
		contentPane1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(934, 10, 96, 21);
		contentPane1.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(1037, 10, 96, 21);
		contentPane1.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(1143, 10, 96, 21);
		contentPane1.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(934, 40, 96, 21);
		contentPane1.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(1037, 40, 96, 21);
		contentPane1.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(1143, 40, 96, 21);
		contentPane1.add(textField_5);
		textField_5.setColumns(10);
		
		panel = new JPanel();
		panel.setBounds(10, 10, 319, 143);
		contentPane1.add(panel);
		new MyThread().start();
	}
	public void paint(Graphics g){
		g = panel.getGraphics();
		g.drawImage(Image, 0, 0, this);
	}
	class MyThread extends Thread{
		@Override
		public void run() {
			for (;;){
				//repaint();
				try { Thread.sleep(30);//  set FPS?
				} catch (InterruptedException e) {    }
			}
		}
	}
}
