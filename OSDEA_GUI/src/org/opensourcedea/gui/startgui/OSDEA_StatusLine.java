package org.opensourcedea.gui.startgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class OSDEA_StatusLine {
	
	private Shell shell;
	private Label stlNotificationLabel;
	private Label stlPositionLabel;
	private Label stlStatusLabel;
	private Label sepLabel;
	private FormData formData;
	private Composite compStatusLine;
	private final long defaultDurationToSleepMs = 5000;
	
	public OSDEA_StatusLine(Shell parentShell) {
		shell = parentShell;
		
	}
	
	public Composite getCompStatusLine(){
		
	    compStatusLine = new Composite(shell, SWT.BORDER);
	    compStatusLine.setLayout(new FormLayout());
	    
	    stlNotificationLabel = new Label(compStatusLine, SWT.NONE);
	    stlNotificationLabel.setText("LabelBar0");
	    formData = new FormData();
	    formData.left = new FormAttachment(0);
	    formData.top = new FormAttachment(0);
	    formData.bottom = new FormAttachment(100);
	    stlNotificationLabel.setLayoutData(formData);
	    
	    
	    stlPositionLabel = new Label(compStatusLine, SWT.NONE);
	    stlPositionLabel.setText("LabelBar1");
	    formData = new FormData();
	    formData.left = new FormAttachment(30);
	    stlPositionLabel.setLayoutData(formData);
	    
	    sepLabel = new Label(compStatusLine, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.VERTICAL);
	    formData = new FormData();
	    formData.height = 15;
	    formData.right = new FormAttachment(stlPositionLabel, -2);
	    sepLabel.setLayoutData(formData);
	    
	    
	    stlStatusLabel = new Label(compStatusLine, SWT.NONE);
	    stlStatusLabel.setText("");
	    formData = new FormData();
	    formData.right = new FormAttachment(60);
//	    formData.left = new FormAttachment(stlPositionLabel, 20);
	    stlStatusLabel.setLayoutData(formData);
	    
	    sepLabel = new Label(compStatusLine, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.VERTICAL);
	    formData = new FormData();
	    formData.height = 15;
	    formData.right = new FormAttachment(stlStatusLabel, -2);
	    sepLabel.setLayoutData(formData);
	    
	    
	    formData = new FormData();
	    formData.left = new FormAttachment(0);
	    formData.right = new FormAttachment(100);
	    formData.bottom = new FormAttachment(100);
		compStatusLine.setLayoutData(formData);
		
		
		return compStatusLine;
		
	}
	
	
	public void setNotificationLabel(final String str) {
		stlNotificationLabel.setText(str);
		stlNotificationLabel.pack();
	}
	
	public void setNotificalLabelDelayStandard(final String str) {
		setLabelTextThenDelete(str, stlNotificationLabel, defaultDurationToSleepMs);
	}

	public void setNotificalLabelDelay(final String str, final int durationBeforeDeleteMs) {
		setLabelTextThenDelete(str, stlNotificationLabel, durationBeforeDeleteMs);
	}

	public void setNotificationLabelBlink(final String str, final int nbTimes) {
		setLabelToBlink(str, stlNotificationLabel, nbTimes);
	}
	
	public void setNotificationLabelBlinkThenClear(final String str, final int nbTimes, final int totalDurationMs) {
		setLabelToBlinkThenClear(stlNotificationLabel, str, nbTimes, totalDurationMs);
	}
	
	
	
	public void setPositionLabel(final String str) {
		stlPositionLabel.setText(str);
		stlPositionLabel.pack();
	}

	public void setPositionLabelDelay(final String str, final int durationBeforeDeleteMs) {
		setLabelTextThenDelete(str, stlPositionLabel, durationBeforeDeleteMs);
	}

	public void setPositionLabelBlink(final String str, final int nbTimes) {
		setLabelToBlink(str, stlPositionLabel, nbTimes);
	}
	
	public void setPositionLabelBlinkThenClear(final String str, final int nbTimes, final int totalDurationMs) {
		setLabelToBlinkThenClear(stlPositionLabel, str, nbTimes, totalDurationMs);
	}
	
	
	
	public void setStatusLabel(final String str) {
		stlStatusLabel.setText(str);
		stlStatusLabel.pack();
	}

	public void setStatusLabelDelay(final String str, final int durationBeforeDeleteMs) {
		setLabelTextThenDelete(str, stlStatusLabel, durationBeforeDeleteMs);
	}

	public void setStatusLabelBlink(final String str, final int nbTimes) {
		setLabelToBlink(str, stlStatusLabel, nbTimes);
	}
	
	public void setStatusLabelBlinkThenClear(final String str, final int nbTimes, final int totalDurationMs) {
		setLabelToBlinkThenClear(stlStatusLabel, str, nbTimes, totalDurationMs);
	}



	
	
	
	
	
	
	
	
	private void setLabelTextThenDelete(String str, final Label label, final long durationBeforeDeleteMs) {
		
		label.setText(str);
		label.pack();

		Thread timeoutThread = new Thread() {
			public void run() {

				try {
					Thread.sleep(durationBeforeDeleteMs);
				} catch (Exception e) {
					//					e.printStackTrace();
				}

				try {
					shell.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							try {
								label.setText("");
							}
							catch (Exception e) {
								//personally i don't care if the widget was disposed or not
							}
						}
					});
				}
				catch (Exception e) {
					//personally i don't care if the widget was disposed or not
				}



			}
		};

		timeoutThread.start();
		
		
	}
	
	
	/**
	 * Sets Label to blink then keep text
	 * @param str
	 * @param label
	 * @param nbTimes
	 */
	private void setLabelToBlink(String str, Label label, int nbTimes) {
		label.setText(str);
		label.pack();

		Thread blinker = null;

		blinker = getBlinker(str, nbTimes, label);
		blinker.start();
		
		label.setText("");
		label.pack();
	}
	
	
	/**
	 * Label Blinks for given number of times and is cleared (setText("");) after totalDurationMs
	 * @param label
	 * @param str
	 * @param nbTimes
	 * @param totalDurationMs
	 */
	public void setLabelToBlinkThenClear(final Label label, final String str, final int nbTimes, final int totalDurationMs) {
		
		label.setText(str);
		label.pack();

		Thread blinker = null;

		blinker = getBlinker(str, nbTimes, label);
		blinker.start();

		final Thread timeoutThread = new Thread() {
			public void run() {

				try {
					Thread.sleep(totalDurationMs);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
				shell.getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						label.setText("");
					}
				});
				}
				catch (Exception e) {
					//personally i don't care if the widget was disposed or not
				}

			}
		};
		timeoutThread.start();
		
	}
	
	


	private Thread getBlinker(final String str, final int nbTimes, final Label label) {
		Thread blinker = new Thread() {
			public void run() {
				for(int i = 0; i < nbTimes; i++) {
					try {
					shell.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							delay(100);
							label.setText("");
							label.pack();
							delay(100);
							label.setText(str);
							label.pack();
						}
					});
					}
					catch (Exception e) {
						// do nothing
					}
				}
			}
		};
		return blinker;
	}




	private void delay(int milliseconds) {
		// Pause for the specified number of milliseconds.
		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e) {
		}
	}


	public Thread getTask(final String str, final int nbTimes, final Label label) {
		return new Thread() {
			public void run() {
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {

						for(int i = 0 ; i < nbTimes; i++) {
							label.setText(str);
							label.pack();

							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							label.setText("");
							label.pack();
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

				});


			}
		};
	}

	
	
}
