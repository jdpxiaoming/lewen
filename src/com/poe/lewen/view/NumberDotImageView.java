package com.poe.lewen.view;

import com.poe.lewen.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NumberDotImageView extends ImageView {
	 private static final int DOT_RADIUS = 3;//选中的点的半径
	  private static final int SPACE = 5; //两个点之间的距离
	  private static final int UNSELECTED_DOT_RADIUS = 3;//未选中点的半径 单位 dp
	  private Context mContext;
	  private int mCurrentPosition = 0;
	  private int mDotRadius; 
	  private int mMaxNumber = 0;
	  private Paint mSelectPaint;
	  private int mSelfHeight;
	  private int mSpace;
	  private Paint mUnSelectPaint;
	  private int mUnSelectedDotRadius;

	  public NumberDotImageView(Context paramContext)
	  {
	    super(paramContext);
	    this.mContext = paramContext;
	    init();
	  }

	  public NumberDotImageView(Context paramContext, AttributeSet paramAttributeSet)
	  {
	    super(paramContext, paramAttributeSet);
	    this.mContext = paramContext;
	    init();
	  }

	  public NumberDotImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	  {
	    super(paramContext, paramAttributeSet, paramInt);
	    this.mContext = paramContext;
	    init();
	  }

	  /**
	   * 数据初始化
	   */
	  private void init()
	  {
		  
	    this.mSpace = (int)dip2Px(this.mContext, SPACE);
	    
	    this.mDotRadius = (int)dip2Px(this.mContext, DOT_RADIUS);
	    
	    this.mUnSelectedDotRadius = (int)dip2Px(this.mContext, UNSELECTED_DOT_RADIUS);
	    
	    refreshWidthHeight();
	    
	    //为选中的点 画布
	    this.mUnSelectPaint = new Paint();
	    this.mUnSelectPaint.setAntiAlias(true);
	    this.mUnSelectPaint.setColor(getResources().getColor(R.color.ssl_dot_image_color_new));
	    this.mUnSelectPaint.setAlpha(255);
	    
	    //选中的点的画布
	    this.mSelectPaint = new Paint();
//	    this.mSelectPaint.reset();
	    this.mSelectPaint.setAntiAlias(true);
//	    this.mSelectPaint.setColor(getResources().getColor(R.color.ssl_white));
	    this.mSelectPaint.setColor(getResources().getColor(R.color.ssl_dot_image_color_selector));
	    this.mSelectPaint.setAlpha(255);//完全不透明
	    
	    //调用onDrawable()
	    invalidate();
	  }

	  /**
	   * 更新图片的 尺寸 高、宽 
	   * 单位:px 像素
	   */
	  private void refreshWidthHeight()
	  {
		 /**
		  * 上下 2px 的padding
		  */
	    this.mSelfHeight = (4 + 2 * this.mDotRadius);
	    
	    setMaxHeight(this.mSelfHeight);
	    setMinimumHeight(this.mSelfHeight);
	  }

	  protected void onDraw(Canvas paramCanvas)
	  {
	    super.onDraw(paramCanvas);
	    
	    int i = 0;
	    for(;i < this.mMaxNumber;i++)
	    {
	    //总的图形（圆）占的长度。以最大的选中的半径圆点来计算
	      int j = 2 * this.mMaxNumber * this.mDotRadius;
	      
	      int k;
	      int x;
	      int y;
	      
	      if (-1 + this.mMaxNumber >= 0)
	      {
	        k = -1 + this.mMaxNumber;
	       //总占居长度
	        int m = j + k * this.mSpace;
	        
	        //计算圆的中心 x的起始位置 
	        x = i * (2 * this.mDotRadius + this.mSpace) + this.mDotRadius + (getWidth() / 2 - m / 2);
	        
	      //计算圆的中心 y的起始位置 
	        y = 2 + this.mDotRadius;
	        
	        
	        if (i != this.mCurrentPosition){//非选中状态 只需要画一个实心点
	 	        k = 0;
//	 	        this.mUnSelectPaint.reset();
//	 	        this.mUnSelectPaint.setColor(-1);
//	 	        this.mUnSelectPaint.setAlpha(200);
//	 	        this.mSelectPaint.setAntiAlias(true);
	 	        
	 	        paramCanvas.drawCircle(x, y, this.mUnSelectedDotRadius, this.mUnSelectPaint);
	 	        
	        }else{//选中状态的 画空心圆并 画 数字
	        	
//	        this.mSelectPaint.reset(); //把已经画好的保存为默认设置
//	        this.mSelectPaint.setColor(-1); //Color.WHITE
//	        this.mSelectPaint.setAlpha(255);//设置可见的程度 或者 说 透明度？没错就是 透明度 0是完全透明255是不透明
//	        this.mSelectPaint.setAntiAlias(true);
	        /**
	        this.mSelectPaint.setStyle(Paint.Style.STROKE);//设置圆的方式为平滑的曲线
	        this.mSelectPaint.setStrokeWidth(2.0F);     //线条的宽度 
	        
	        paramCanvas.drawCircle(x, y, this.mDotRadius, this.mSelectPaint);
	        //写 文字。
	        this.mSelectPaint.reset();
	        this.mSelectPaint.setColor(-1);
	        this.mSelectPaint.setAlpha(255);
	        this.mSelectPaint.setAntiAlias(true);
	        this.mSelectPaint.setTextSize(2 * this.mDotRadius);
	        Typeface localTypeface = Typeface.create("宋体", 1);
	        this.mSelectPaint.setTypeface(localTypeface);
	        
	        paramCanvas.drawText(i + 1 + "", x - this.mDotRadius / 2, y + 2 * this.mDotRadius / 3, this.mSelectPaint);
	        */
	        paramCanvas.drawCircle(x, y, this.mDotRadius, this.mSelectPaint);
	        
	        }
	      }
	    }
	  }

	  /**
	   * 外部调用方法
	   * @param paramInt1  最大的页数
	   * @param paramInt2 当前选中的页数
	   */
	  public void refresh(int paramInt1, int paramInt2)
	  {
	    if (paramInt2<=paramInt1)
	    {
	      this.mMaxNumber = paramInt1; //3
	      this.mCurrentPosition = paramInt2; //1
	      
	      refreshWidthHeight();
	      
	      invalidate();
	      
	      return;
	    }
	  }
	  
	  /**
	   * 转化 从dip到 px
	   * @param paramContext
	   * @param paramFloat
	   * @return
	   */
	  public static float dip2Px(Context paramContext, float paramFloat)
	  {
	    return 0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density;
	  }
}
