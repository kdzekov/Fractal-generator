import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


class ComplexNumber {

    public final double real, imaginary;

    public ComplexNumber(double real, double imaginary)  {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber()  { this(0,0); }
    public ComplexNumber(ComplexNumber cn)  { this(cn.real, cn.imaginary); }

    public ComplexNumber add(ComplexNumber x) {
        return new ComplexNumber(real + x.real, imaginary + x.imaginary);
    }

 
    public ComplexNumber multiply (ComplexNumber x) {
        return new ComplexNumber(
            (real * x.real) - (imaginary * x.imaginary),
            (real * x.imaginary) + (imaginary * x.real)
        );

    }

    public double getMagnitude() { return Math.sqrt(real*real + imaginary*imaginary); }
    public String toString (){ return real + "+" + imaginary + "i"; }           
}


class FractalCanvas extends Canvas{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean draw = false;
    final int size_width = 800;
    final int size_height = 600;
    final int depth = 500;
    
    double zc_real = 0.0;
    double zc_image = 0.0;

    ComplexNumber[][] cnSet = new ComplexNumber[size_width][size_height];
    
    
	
	public FractalCanvas() {
		// TODO Auto-generated constructor stub
		
		 double lx = -1.75;
         double ly = -1.75;
         double ux = 1.75;
         double uy = 1.75;
         double xStep = (ux - lx)/size_width;
         double yStep = (uy - ly)/size_height;
         for( int i = 0; i < size_width; i ++){
            for (int j = 0; j < size_height; j++){
                cnSet[i][j] = new ComplexNumber(lx + i*xStep, uy - j*yStep);
            }
         }
	}
	
	private int getIterations(ComplexNumber c){

        ComplexNumber z = new ComplexNumber();
       // ComplexNumber zc = new ComplexNumber(-0.25,0.75);
        ComplexNumber zc = new ComplexNumber(this.zc_real,this.zc_image);
        z = c;
        
        int iterations = 0;
        for(; iterations < depth; iterations++) {
            if (z.getMagnitude() > 2 ){ break; }
            z = z.multiply(z).add(zc);
        }
        return iterations;
    }
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub		
		if (draw){
			
			for (int i = 0; i < size_width; i++){
                for (int j = 0; j < size_height; j++){
                    int iterations = getIterations(cnSet[i][j]);
                    g.setColor((iterations==depth) ? Color.BLACK : getColor(iterations) );
                    g.fillRect(i ,j ,1,1);
                }
            }
			
		}
	}
	
	private Color getColor(int iterations){
		//Color backColor=null;
		
		switch(iterations % 6)
		{
			case 1: return Color.YELLOW;
			case 2: return Color.ORANGE;
			case 3: return Color.RED;
			case 4: return Color.GREEN;
			case 5: return Color.CYAN;
			default: return Color.BLUE;	
			
		}	
	}
	
	public void setDrawMode(boolean dm){
		this.draw=dm;
	}
	
	public void setZC(double real, double imagery){
		this.zc_real = real;
		this.zc_image = imagery;
	}
	
}

public class FractalGeneratorClass {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FractalGeneratorClass window = new FractalGeneratorClass();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FractalGeneratorClass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Julia fractal set generator");
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	
		JPanel panel = new JPanel();
		//panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		panel.setLayout(null);
		//panel.setBackground(Color.RED);
		frame.getContentPane().add(panel);
		
		FractalCanvas fcanv = new FractalCanvas();
		fcanv.setBounds(0, 0, 800, 700);
		fcanv.setBackground(Color.darkGray);
		panel.add(fcanv,BorderLayout.WEST);
		
		JButton btnGenerateFract = new JButton("Generate fractal");
		btnGenerateFract.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnGenerateFract.setForeground(new Color(0, 0, 0));
		btnGenerateFract.setBackground(Color.ORANGE);
		

		btnGenerateFract.setBounds(825, 23, 144, 48);
		panel.add(btnGenerateFract);
		
		JLabel lblRealval = new JLabel("real_val");
		lblRealval.setBounds(890, 110, 46, 14);
		
		JSlider zc_real = new JSlider();
		zc_real.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				//lblRealval.setText((new Integer(zc_real.getValue()).toString()));
				lblRealval.setText((new Double((new Integer(zc_real.getValue()).doubleValue()/100))).toString());
			}
		});
		zc_real.setToolTipText("");
		zc_real.setMinimum(-100);
		zc_real.setBounds(821, 135, 148, 26);
		zc_real.setMajorTickSpacing(1);
		zc_real.setPaintLabels(true);
		zc_real.setValue(-25);
		
		
		/*Real num slider labels */
		Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();	
		labelTable.put(100, new JLabel("1"));
	    labelTable.put(0, new JLabel("0"));
	    labelTable.put(-100, new JLabel("-1"));
	    zc_real.setLabelTable( labelTable );
	    zc_real.setPaintLabels(true);
	    
	    panel.add(zc_real);
		
		JLabel lblZcReal = new JLabel("ZC real:");
		lblZcReal.setBounds(825, 110, 79, 14);
		panel.add(lblZcReal);
		
		JLabel lblImgVal = new JLabel("New label");
		lblImgVal.setBounds(890, 208, 46, 14);
		
		JSlider zc_image = new JSlider();
		zc_image.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {		
				lblImgVal.setText((new Double((new Integer(zc_image.getValue()).doubleValue()/100))).toString());
			}
		});
		zc_image.setMinorTickSpacing(1);
		zc_image.setMinimum(-100);
		zc_image.setBounds(820, 229, 144, 26);
		zc_image.setPaintLabels(true);
		zc_image.setValue(75);

		zc_image.setLabelTable( labelTable );
		zc_image.setPaintLabels(true);
		
		panel.add(zc_image);
		
		JLabel lblZcImag = new JLabel("ZC Imag:");
		lblZcImag.setBounds(826, 208, 79, 14);
		panel.add(lblZcImag);
		
		
		//lblRealval.setText(((new Integer(zc_real.getValue())).doubleValue()/100).toString()));
		lblRealval.setText((new Double((new Integer(zc_real.getValue()).doubleValue()/100))).toString());
		panel.add(lblRealval);
		
		lblImgVal.setText((new Double((new Integer(zc_image.getValue()).doubleValue()/100))).toString());
		panel.add(lblImgVal);
		
		btnGenerateFract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//here the action
				//JOptionPane.showMessageDialog(frame, "The real value is: "+zc_real.getValue()+" The imagery number is: "+zc_image.getValue());
				fcanv.setDrawMode(true);
				fcanv.setZC((new Integer(zc_real.getValue())).doubleValue()/100.00 , (new Integer(zc_image.getValue())).doubleValue()/100.00);
				fcanv.repaint();
			}
		});
		
	}
}
