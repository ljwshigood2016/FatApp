package com.wearme.zfc.widget;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wearme.zfc.R;


/**
 * Created by Dacer on 11/4/13.
 * Edited by Lee youngchan 21/1/14
 * Edited by dector 30-Jun-2014
 */
public class LineView extends View {
    private int mViewHeight;
    //drawBackground
    private boolean autoSetDataOfGird = true;
    private boolean autoSetGridWidth = true;
    private int dataOfAGird = 10;
    private int bottomTextHeight = 0;
    private ArrayList<String> bottomTextList = new ArrayList<String>();
    
    private ArrayList<ArrayList<Integer>> dataLists;
    private ArrayList<Integer> dataList;
    
    private ArrayList<Integer> xCoordinateList = new ArrayList<Integer>();
    private ArrayList<Integer> yCoordinateList = new ArrayList<Integer>();
    
    private ArrayList<ArrayList<Dot>> drawDotLists = new ArrayList<ArrayList<Dot>>();
    private ArrayList<Dot> drawDotList = new ArrayList<Dot>();
    
    private Paint bottomTextPaint = new Paint();
    private int bottomTextDescent;
    
    private ArrayList<Float> fdataLists = new ArrayList<Float>();

    public ArrayList<Float> getFdataLists() {
		return fdataLists;
	}
	public void setFdataLists(ArrayList<Float> fdataLists) {
		this.fdataLists = fdataLists;
	}

	//popup
    private Paint popupTextPaint = new Paint();
    private final int bottomTriangleHeight = 12;
    public boolean showPopup = true; 

	private Dot pointToSelect;
	private Dot selectedDot;

    public Dot getSelectedDot() {
		return selectedDot;
	}
	public void setSelectedDot() {
		postInvalidate();
	}

	private int topLineLength = Utils.dip2px(getContext(), 12);; // | | ←this
                                                                   //-+-+-
    private int sideLineLength = Utils.dip2px(getContext(),45)/3*2;// --+--+--+--+--+--+--
                                                                     //  ↑this
    private int backgroundGridWidth = Utils.dip2px(getContext(),45);

    //Constants
    private final int popupTopPadding = Utils.dip2px(getContext(),2);
    private final int popupBottomMargin = Utils.dip2px(getContext(),5);
    private final int bottomTextTopMargin = Utils.sp2px(getContext(),5);
    private final int bottomLineLength = Utils.sp2px(getContext(), 22);
    private final int DOT_INNER_CIR_RADIUS = Utils.dip2px(getContext(), 2);
    private final int DOT_OUTER_CIR_RADIUS = Utils.dip2px(getContext(),5);
    private final int MIN_TOP_LINE_LENGTH = Utils.dip2px(getContext(),12);
    private final int MIN_VERTICAL_GRID_NUM = 4;
    private final int MIN_HORIZONTAL_GRID_NUM = 1;
    private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");
    private final int BOTTOM_TEXT_COLOR = Color.parseColor("#2a5b83");
    
    public static final int SHOW_POPUPS_All = 1;
    public static final int SHOW_POPUPS_MAXMIN_ONLY = 2;
    public static final int SHOW_POPUPS_NONE = 3;

    private int showPopupType = SHOW_POPUPS_NONE;
    public void setShowPopup(int popupType) {
		this.showPopupType = popupType;
	}

    private Boolean drawDotLine = false;
    
    private String[] colorArray = {"#e74c3c","#2980b9","#1abc9c"};
    
    private int[] popupColorArray = {R.drawable.pop_chart,R.drawable.pop_chart,R.drawable.pop_chart};

    private final Point tmpPoint = new Point();
    
	public void setDrawDotLine(Boolean drawDotLine) {
		this.drawDotLine = drawDotLine;
	}

	private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for(ArrayList<Dot> data : drawDotLists){
            	for(Dot dot : data){
                    dot.update();
                    if(!dot.isAtRest()){
                        needNewFrame = true;
                    }
                }
            }
            if (needNewFrame) {
                post(this);
            }
            invalidate();
        }
    };

    public LineView(Context context){
        this(context,null);
    }
    public LineView(Context context, AttributeSet attrs){
        super(context, attrs);
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(Utils.sp2px(getContext(), 12));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setColor(Color.parseColor("#fd660b"));
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(Utils.sp2px(getContext(),22));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);
    }
    
    private boolean isRefenceLine ;

    public boolean isRefenceLine() {
		return isRefenceLine;
	}
    
	public void setRefenceLine(boolean isRefenceLine) {
		this.isRefenceLine = isRefenceLine;
	}
	
	/**
     * dataList will be reset when called is method.
     * @param bottomTextList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomTextList){
        this.dataList = null;
        this.bottomTextList = bottomTextList;

        Rect r = new Rect();
        int longestWidth = 0;
        String longestStr = "";
        bottomTextDescent = 0;
        for(String s:bottomTextList){
            bottomTextPaint.getTextBounds(s,0,s.length(),r);
            if(bottomTextHeight<r.height()){
                bottomTextHeight = r.height();
            }
            if(autoSetGridWidth&&(longestWidth<r.width())){
                longestWidth = r.width();
                longestStr = s;
            }
            if(bottomTextDescent<(Math.abs(r.bottom))){
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        if(autoSetGridWidth){
            if(backgroundGridWidth<longestWidth){
                backgroundGridWidth = longestWidth+(int)bottomTextPaint.measureText(longestStr,0,1);
            }
            if(sideLineLength<longestWidth/2){
                sideLineLength = longestWidth/2;
            }
        }

        refreshXCoordinateList(getHorizontalGridNum());
    }

    /**
     *
     * @param dataLists The Integer ArrayLists for showing,
     *                 dataList.size() must < bottomTextList.size()
     */
    public void setDataList(ArrayList<ArrayList<Integer>> dataLists){
    	
    	selectedDot = null;
        this.dataLists = dataLists;
        for(ArrayList<Integer> list : dataLists){
        	if(list.size() > bottomTextList.size()){
                throw new RuntimeException("dacer.LineView error:" +
                        " dataList.size() > bottomTextList.size() !!!");
            }
        }
        int biggestData = 0;
        for(ArrayList<Integer> list : dataLists){
        	if(autoSetDataOfGird){
                for(Integer i:list){
                    if(biggestData<i){
                        biggestData = i;
                    }
                }
        	}
        	dataOfAGird = 1;
        	while(biggestData/10 > dataOfAGird){
        		dataOfAGird *= 10;
        	}
        }
        
        refreshAfterDataChanged();
        showPopup = true;
        setMinimumWidth(0); // It can help the LineView reset the Width,
                                // I don't know the better way..
        
        postInvalidate();
    }
    
    

    private void refreshAfterDataChanged(){
        int verticalGridNum = getVerticalGridlNum();
        refreshTopLineLength(verticalGridNum);
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
    }
    
    private int mType ;
    
	public int getmType() {
		return mType;
	}
    
	public void setmType(int mType) {
		this.mType = mType;
	}
	
	private int getVerticalGridlNum(){
        int verticalGridNum = MIN_VERTICAL_GRID_NUM;
        if(dataLists != null && !dataLists.isEmpty()){
        	for(ArrayList<Integer> list : dataLists){
	        	for(Integer integer : list){
	        		if(verticalGridNum < (integer+1)){
	        			verticalGridNum = integer+1;
	        		}
	        	}
        	}
        }
        
        float chart_MaxValue = 0.0f;
        int maxGridNum = 0 ;
		for(int i = 0 ;i < fdataLists.size();i++){
			float values = fdataLists.get(i);
			 if (chart_MaxValue < values) {
				 chart_MaxValue = values;
			 }
		}	
		
		switch (mType) {
		  case 0:
	            //脂肪含量
	            if (chart_MaxValue > 60) {
	            	chart_MaxValue = chart_MaxValue * 1.2f;
	            }else{
	            	chart_MaxValue = 60;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            
	            break;
	        case 1:
	            //卡路里
	            if (chart_MaxValue > 1600) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 1600;
	            }
	            maxGridNum = (int)chart_MaxValue ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            
	            break;
	        case 2:
	            //水分含量
	            if (chart_MaxValue > 90) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 90;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            
	            
	            break;
	        case 3:
	            //肌肉含量
	            if (chart_MaxValue > 60) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 60;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            
	            break;
	        case 4:
	            //内脏脂肪含量
	            if (chart_MaxValue > 60) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 60;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            
	            break;
	        case 5:
	            //骨骼
	            if (chart_MaxValue > 5) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 5;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            
	            break;
	        case 6:
	            //体重
	            if (chart_MaxValue > 180) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 180;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            break;
	        case 7:
	            //BMI
	            if (chart_MaxValue > 60) {
	            	chart_MaxValue = chart_MaxValue*1.2f;
	            }else{
	            	chart_MaxValue = 60;
	            }
	            
	            maxGridNum = (int)chart_MaxValue * 10 ;
	            if(verticalGridNum < maxGridNum){
	            	verticalGridNum = maxGridNum + 1;
	            }
	            break;
	        default:
	            break;
		}
        
        return verticalGridNum;
    }
    
    
    private int getHorizontalGridNum(){
        int horizontalGridNum = bottomTextList.size()-1;
        if(horizontalGridNum<MIN_HORIZONTAL_GRID_NUM){
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }

    private void refreshXCoordinateList(int horizontalGridNum){
        xCoordinateList.clear();
        for(int i=0;i<(horizontalGridNum+1);i++){
            xCoordinateList.add(sideLineLength + backgroundGridWidth*i);
        }

    }

    private void refreshYCoordinateList(int verticalGridNum){
        yCoordinateList.clear();
        for(int i=0;i<(verticalGridNum+1);i++){
            yCoordinateList.add(topLineLength +
                    ((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin-
                            bottomLineLength-bottomTextDescent)*i/(verticalGridNum)));
        }
    }

    private void refreshDrawDotList(int verticalGridNum){
    	drawDotLists.clear();
        if(dataLists != null && !dataLists.isEmpty()){
    		if(drawDotLists.size() == 0){
    			for(int k = 0; k < dataLists.size(); k++){
    				drawDotLists.add(new ArrayList<LineView.Dot>());
    			}
    		}
        	for(int k = 0; k < dataLists.size(); k++){
        		int drawDotSize = drawDotLists.get(k).isEmpty()? 0:drawDotLists.get(k).size();
        		
        		for(int i = 0 ; i < dataLists.get(k).size();i++){
                    int x = xCoordinateList.get(i);
                    int y = yCoordinateList.get(verticalGridNum - dataLists.get(k).get(i));
                    if(i>drawDotSize-1){
                        drawDotLists.get(k).add(new Dot(x, 0, x, y, dataLists.get(k).get(i),k,fdataLists.get(i)));
                    }else{
                        drawDotLists.get(k).set(i, drawDotLists.get(k).get(i).setTargetData(x,y,dataLists.get(k).get(i),k,fdataLists.get(i)));
                    }
                }
        		
        		int temp = drawDotLists.get(k).size() - dataLists.get(k).size();
        		for(int i=0; i<temp; i++){
        			drawDotLists.get(k).remove(drawDotLists.get(k).size()-1);
        		}
        	}
        }
        removeCallbacks(animator);
        post(animator);
    }

    private void refreshTopLineLength(int verticalGridNum){
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        // But this code not so good.
        if((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin)/
                (verticalGridNum+2)<getPopupHeight()){
            topLineLength = getPopupHeight()+DOT_OUTER_CIR_RADIUS+DOT_INNER_CIR_RADIUS+2;
        }else{
            topLineLength = MIN_TOP_LINE_LENGTH;
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	mCanvas = canvas ;
    	
        drawBackgroundLines(canvas);
        drawLines(canvas);
        drawDots(canvas);
        
        for(int k=0; k < drawDotLists.size(); k++){
        	int MaxValue = Collections.max(dataLists.get(k));
        	int MinValue = Collections.min(dataLists.get(k));
        	for(int i = 0 ;i < drawDotLists.get(k).size();i++){
        		Dot d = drawDotLists.get(k).get(i);
        		if(showPopupType == SHOW_POPUPS_All){
        			drawPopup(canvas, String.valueOf(d.fdata), d.setupPoint(tmpPoint),popupColorArray[k%3]);
        		}else if(showPopupType == SHOW_POPUPS_MAXMIN_ONLY){
        			if(!isRefenceLine){
        				
        				if(d.data == MaxValue){
	        				if(d.data != 0){
	        					drawPopup(canvas, String.valueOf(d.fdata), d.setupPoint(tmpPoint),popupColorArray[k%3]);
	        				}
	        			}
	        			if(d.data == MinValue){
	        				if(d.data != 0){
	        					drawPopup(canvas, String.valueOf(d.fdata), d.setupPoint(tmpPoint),popupColorArray[k%3]);
	        				}
	        			}
        				
        			}else {
        				if(i < drawDotLists.get(k).size() -  4){
        					
        					//if(d.data == MaxValue){
    	        				if(d.data != 0){
    	        					drawPopup(canvas, String.valueOf(d.fdata), d.setupPoint(tmpPoint),popupColorArray[k%3]);
    	        				}
    	        			//}
    	        			//if(d.data == MinValue){
    	        				if(d.data != 0){
    	        					drawPopup(canvas, String.valueOf(d.fdata), d.setupPoint(tmpPoint),popupColorArray[k%3]);
    	        				}
    	        			//}
        				}
        			}
        		}
        	}
        }
        
        if(showPopup && selectedDot != null){
        	drawPopup(canvas,
                    String.valueOf(selectedDot.fdata),
                    selectedDot.setupPoint(tmpPoint),popupColorArray[selectedDot.linenumber%3]);
        }
    }

    /**
     *
     * @param canvas  The canvas you need to draw on.
     * @param point   The Point consists of the x y coordinates from left bottom to right top.
     *                Like is
     *                
     *                3
     *                2
     *                1
     *                0 1 2 3 4 5
     */
    private void drawPopup(Canvas canvas,String num, Point point,int PopupColor){
        boolean singularNum = (num.length() == 1);
        int sidePadding = Utils.dip2px(getContext(),singularNum? 8:5);
        int x = point.x;
        int y = point.y - Utils.dip2px(getContext(),5);
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(num,0,num.length(),popupTextRect);
        Rect r = new Rect(x-popupTextRect.width()/2-sidePadding,
                y - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                x + popupTextRect.width()/2+sidePadding,
                y+popupTopPadding-popupBottomMargin);

        NinePatchDrawable popup = (NinePatchDrawable)getResources().getDrawable(PopupColor);
        popup.setBounds(r);
        popup.draw(canvas);
        canvas.drawText(num, x, y-bottomTriangleHeight-popupBottomMargin, popupTextPaint);
    }

    private int getPopupHeight(){
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9",0,1,popupTextRect);
        Rect r = new Rect(-popupTextRect.width()/2,
                 - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                 + popupTextRect.width()/2,
                +popupTopPadding-popupBottomMargin);
        return r.height();
    }
    
    private Canvas mCanvas ;
    
    private Paint bigCirPaint ;
    
    private int mCurrentLength  = 0 ;
    
    private int mCurrentPosition = -1 ;
    
    Runnable runnableCircle = new Runnable() {
		
		@Override
		public void run() {
			
			ArrayList<Dot> drawDotList =  drawDotLists.get(0);
			mCurrentLength = drawDotList.size();
			mCurrentPosition++ ;
			if(mCurrentPosition < mCurrentLength){
				mCanvas.drawCircle(drawDotList.get(mCurrentPosition).x,drawDotList.get(mCurrentPosition).y,DOT_OUTER_CIR_RADIUS,bigCirPaint);
				postDelayed(this, 250);
			}
		}
	};
	
    
    private void drawDots(Canvas canvas){
    	mCurrentLength = 0 ;
    	mCurrentPosition = -1 ;
    	
        bigCirPaint = new Paint();
        bigCirPaint.setAntiAlias(true);
    	bigCirPaint.setColor(Color.parseColor("#5d0de0"));
        if(drawDotLists!=null && !drawDotLists.isEmpty()){
        	for(int k=0; k < drawDotLists.size(); k++){
        		if(!isRefenceLine){
        			for(int i = 0 ;i < drawDotLists.get(k).size();i++){
	        			Dot dot = drawDotLists.get(k).get(i);
	        			canvas.drawCircle(dot.x,dot.y,DOT_OUTER_CIR_RADIUS,bigCirPaint);
	        		}
        		}else {
	        		for(int i = 0 ;i < drawDotLists.get(k).size() - 4;i++){
	        			Dot dot = drawDotLists.get(k).get(i);
	        			if(dot.data > 0){
	        				canvas.drawCircle(dot.x,dot.y,DOT_OUTER_CIR_RADIUS,bigCirPaint);
	        			}
	        		}
        		}
        	}
        }
    }

    private void drawLines(Canvas canvas){
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(Utils.dip2px(getContext(), 2));
        
        
        Paint paint = new Paint();      
        paint.setStyle(Paint.Style.STROKE);      
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);      
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);      
        paint.setPathEffect(effects); 
        
        
        for(int k = 0; k<drawDotLists.size(); k ++){
        	linePaint.setColor(Color.parseColor("#5d0de0"));
        	
        	if(!isRefenceLine){
        		
        		for(int i=0; i<drawDotLists.get(k).size()- 1; i++){
		            canvas.drawLine(drawDotLists.get(k).get(i).x,
		                    drawDotLists.get(k).get(i).y,
		                    drawDotLists.get(k).get(i+1).x,
		                    drawDotLists.get(k).get(i+1).y,
		                    linePaint);
		        }
        		
        	}else{
        		
		        for(int i=0; i<drawDotLists.get(k).size()- 1 - 4; i++){
		        	if( drawDotLists.get(k).get(i).data > 0 &&  drawDotLists.get(k).get(i+1).data > 0){
		        		 canvas.drawLine(drawDotLists.get(k).get(i).x,
				                    drawDotLists.get(k).get(i).y,
				                    drawDotLists.get(k).get(i+1).x,
				                    drawDotLists.get(k).get(i+1).y,
				                    linePaint);
		        	}
		        }
		        
		        int xlowPrefence = drawDotLists.get(k).size() - 1 - 4 ;
		       
		        int ylowPreference = drawDotLists.get(k).size() - 1 - 4 + 1;
		        
		        //draw refence line lower
		        
		        Path path = new Path();       
		        path.moveTo(drawDotLists.get(k).get(0).x, drawDotLists.get(k).get(ylowPreference).y);      
		        path.lineTo(drawDotLists.get(k).get(xlowPrefence).x,drawDotLists.get(k).get(ylowPreference + 1).y); 
		        canvas.drawPath(path, paint); 
		        
		        //draw refence line hight 
		        
		        Path path1 = new Path();       
		        path1.moveTo(drawDotLists.get(k).get(0).x, drawDotLists.get(k).get(ylowPreference + 2).y);      
		        path1.lineTo(drawDotLists.get(k).get(xlowPrefence).x, drawDotLists.get(k).get(ylowPreference + 3).y); 
		        canvas.drawPath(path1, paint); 
        	}
        }
    }
    
    
     private void drawBackgroundLines(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dip2px(getContext(),1f));
        paint.setColor(BACKGROUND_LINE_COLOR);
        PathEffect effects = new DashPathEffect(new float[]{10,5,10,5}, 1);

        //draw vertical lines
        for(int i=0;i<xCoordinateList.size();i++){
           /*canvas.drawLine(xCoordinateList.get(i),
                    0,
                    xCoordinateList.get(i),
                    mViewHeight - bottomTextTopMargin - bottomTextHeight-bottomTextDescent,
                    paint);*/
        }
        
        //draw dotted lines
      paint.setPathEffect(effects);
      Path dottedPath = new Path();
      for(int i=0;i<yCoordinateList.size();i++){
          if((yCoordinateList.size()-1-i)%dataOfAGird == 0){
              dottedPath.moveTo(0, yCoordinateList.get(i));
              dottedPath.lineTo(getWidth(), yCoordinateList.get(i));
             // canvas.drawPath(dottedPath, paint);
          }
      }
    	  //draw bottom text
      if(bottomTextList != null){
    	  if(!isRefenceLine){
    		  for(int i=0;i<bottomTextList.size() ;i++){
	    		  canvas.drawText(bottomTextList.get(i), sideLineLength+backgroundGridWidth*i, mViewHeight-bottomTextDescent, bottomTextPaint);
	    	  }
    	  }else{
	    	  for(int i=0;i<bottomTextList.size() - 4;i++){
	    		  canvas.drawText(bottomTextList.get(i), sideLineLength+backgroundGridWidth*i, mViewHeight-bottomTextDescent, bottomTextPaint);
	    	  }
    	  }
      }
      
      if(!drawDotLine){
    	//draw solid lines
          for(int i=0;i<yCoordinateList.size();i++){
              if((yCoordinateList.size()-1-i)%dataOfAGird == 0){
                  //canvas.drawLine(0,yCoordinateList.get(i),getWidth(),yCoordinateList.get(i),paint);
              }
          }
      }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
//        mViewHeight = MeasureSpec.getSize(measureSpec);
        refreshAfterDataChanged();
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureWidth(int measureSpec){
        int horizontalGridNum = getHorizontalGridNum();
        int preferred = backgroundGridWidth*horizontalGridNum+sideLineLength*2;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec){
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pointToSelect = findPointAt((int) event.getX(), (int) event.getY());
            Log.e("liujw","#############event.getX() "+(int)event.getX()+"################event.getY() "+(int)event.getY());
            Log.e("liujw","#############pointToSelect "+pointToSelect);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (pointToSelect != null) {
            	Log.e("liujw","#############pointToSelect != null ");
                selectedDot = pointToSelect;
                pointToSelect = null;
                postInvalidate();
            }
        }

        return true;
    }

    private Dot findPointAt(int x, int y) {
        if (drawDotLists.isEmpty()) {
            return null;
        }

        final int width = backgroundGridWidth/2;
        final Region r = new Region();

        for (ArrayList<Dot> data : drawDotLists) {
            for (Dot dot : data) {
                final int pointX = dot.x;
                final int pointY = dot.y;

                r.set(pointX - width, pointY - width, pointX + width, pointY + width);
                if (r.contains(x, y)){
                    return dot;
                }
            }
        }

        return null;
    }


    
    class Dot{
        int x;
        int y;
        int data;
        int targetX;
        int targetY;
        int linenumber;
        float fdata ;
        int velocity = Utils.dip2px(getContext(),18);

        Dot(int x,int y,int targetX,int targetY,Integer data,int linenumber,float fdata){
            this.x = x;
            this.y = y;
            this.linenumber = linenumber;
            this.fdata = fdata ;
            setTargetData(targetX, targetY,data,linenumber,fdata);
        }

        Point setupPoint(Point point) {
            point.set(x, y);
            return point;
        }

        Dot setTargetData(int targetX,int targetY,Integer data,int linenumber,float fdata){
            this.targetX = targetX;
            this.targetY = targetY;
            this.data = data;
            this.linenumber = linenumber;
            return this;
        }

        boolean isAtRest(){
            return (x==targetX)&&(y==targetY);
        }

        void update(){
            x = updateSelf(x, targetX, velocity);
            y = updateSelf(y, targetY, velocity);
        }

        private int updateSelf(int origin, int target, int velocity){
            if (origin < target) {
                origin += velocity;
            } else if (origin > target){
                origin-= velocity;
            }
            if(Math.abs(target-origin)<velocity){
                origin = target;
            }
            return origin;
        }
    }
}
