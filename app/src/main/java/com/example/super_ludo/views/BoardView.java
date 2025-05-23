package com.example.super_ludo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.super_ludo.R;
import com.example.super_ludo.models.BoardCell;
import com.example.super_ludo.models.CellType;
import com.example.super_ludo.models.GameBoard;
import com.example.super_ludo.models.PlayerColor;
import com.example.super_ludo.models.Spaceship;

import java.util.List;

/**
 * Custom view for rendering the game board.
 */
public class BoardView extends View {

    private GameBoard gameBoard;
    private int cellSize;
    private int boardSize;
    private Paint gridPaint;
    private Paint cellPaint;
    private Paint shipPaint;
    private BoardCellClickListener cellClickListener;

    // Cell selection highlight
    private BoardCell selectedCell;
    private Paint selectionPaint;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paints
        gridPaint = new Paint();
        gridPaint.setColor(ContextCompat.getColor(getContext(), R.color.boardGrid));
        gridPaint.setStrokeWidth(2f);
        gridPaint.setStyle(Paint.Style.STROKE);

        cellPaint = new Paint();
        cellPaint.setStyle(Paint.Style.FILL);

        shipPaint = new Paint();
        shipPaint.setStyle(Paint.Style.FILL);

        selectionPaint = new Paint();
        selectionPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        selectionPaint.setStrokeWidth(4f);
        selectionPaint.setStyle(Paint.Style.STROKE);

        // Set default board size
        boardSize = 11; // Default 11x11 grid
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        invalidate();
    }

    public void setSelectedCell(BoardCell cell) {
        this.selectedCell = cell;
        invalidate();
    }

    public void setBoardCellClickListener(BoardCellClickListener listener) {
        this.cellClickListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Make the view square based on the minimum dimension
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.min(width, height);

        // Calculate cell size
        cellSize = size / boardSize;

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gameBoard == null) return;

        // Draw the board
        drawBoard(canvas);

        // Draw the spaceships
        drawSpaceships(canvas);

        // Draw selection highlight if a cell is selected
        if (selectedCell != null) {
            drawCellSelection(canvas, selectedCell);
        }
    }

    private void drawBoard(Canvas canvas) {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                BoardCell cell = gameBoard.getCell(x, y);
                if (cell != null) {
                    drawCell(canvas, cell);
                }
            }
        }
    }

    private void drawCell(Canvas canvas, BoardCell cell) {
        int x = cell.getX() * cellSize;
        int y = cell.getY() * cellSize;

        Rect cellRect = new Rect(x, y, x + cellSize, y + cellSize);

        // Draw cell based on its type
        switch (cell.getType()) {
            case EMPTY:
                // Don't draw empty cells
                break;

            case PATH:
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.boardBackground));
                canvas.drawRect(cellRect, cellPaint);
                canvas.drawRect(cellRect, gridPaint);
                break;

            case SAFE_ZONE:
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.safeZone));
                canvas.drawRect(cellRect, cellPaint);
                break;

            case BASE:
                drawColoredCell(canvas, cellRect, cell.getColor());
                break;

            case START:
                drawColoredCell(canvas, cellRect, cell.getColor());
                // Draw a star or some indicator that this is a start position
                drawStar(canvas, x + cellSize/2, y + cellSize/2, cellSize/4);
                break;

            case HOME_PATH:
                drawColoredCell(canvas, cellRect, cell.getColor());
                break;

            case HOME:
                // Draw home as a special cell
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.boardBackground));
                canvas.drawRect(cellRect, cellPaint);

                // Draw colored segments for each player's home
                drawHomeCell(canvas, cellRect);
                break;
        }
    }

    private void drawColoredCell(Canvas canvas, Rect rect, PlayerColor color) {
        if (color == null) return;

        switch (color) {
            case BLUE:
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerBlue));
                break;
            case GREEN:
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerGreen));
                break;
            case RED:
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerRed));
                break;
            case YELLOW:
                cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerYellow));
                break;
        }

        canvas.drawRect(rect, cellPaint);
        canvas.drawRect(rect, gridPaint);
    }

    private void drawHomeCell(Canvas canvas, Rect rect) {
        // Draw a divided cell with each player's color in a quadrant
        int centerX = rect.left + rect.width() / 2;
        int centerY = rect.top + rect.height() / 2;

        // Blue quadrant (bottom-right)
        Path bluePath = new Path();
        bluePath.moveTo(centerX, centerY);
        bluePath.lineTo(rect.right, centerY);
        bluePath.lineTo(rect.right, rect.bottom);
        bluePath.lineTo(centerX, rect.bottom);
        bluePath.close();
        cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerBlue));
        canvas.drawPath(bluePath, cellPaint);

        // Green quadrant (top-right)
        Path greenPath = new Path();
        greenPath.moveTo(centerX, centerY);
        greenPath.lineTo(rect.right, centerY);
        greenPath.lineTo(rect.right, rect.top);
        greenPath.lineTo(centerX, rect.top);
        greenPath.close();
        cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerGreen));
        canvas.drawPath(greenPath, cellPaint);

        // Red quadrant (top-left)
        Path redPath = new Path();
        redPath.moveTo(centerX, centerY);
        redPath.lineTo(centerX, rect.top);
        redPath.lineTo(rect.left, rect.top);
        redPath.lineTo(rect.left, centerY);
        redPath.close();
        cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerRed));
        canvas.drawPath(redPath, cellPaint);

        // Yellow quadrant (bottom-left)
        Path yellowPath = new Path();
        yellowPath.moveTo(centerX, centerY);
        yellowPath.lineTo(centerX, rect.bottom);
        yellowPath.lineTo(rect.left, rect.bottom);
        yellowPath.lineTo(rect.left, centerY);
        yellowPath.close();
        cellPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerYellow));
        canvas.drawPath(yellowPath, cellPaint);

        // Draw grid lines
        canvas.drawRect(rect, gridPaint);
        canvas.drawLine(centerX, rect.top, centerX, rect.bottom, gridPaint);
        canvas.drawLine(rect.left, centerY, rect.right, centerY, gridPaint);
    }

    private void drawStar(Canvas canvas, int centerX, int centerY, int radius) {
        Paint starPaint = new Paint();
        starPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        starPaint.setStyle(Paint.Style.FILL);

        Path starPath = new Path();
        float innerRadius = radius * 0.4f;

        // Calculate the points of the star
        for (int i = 0; i < 10; i++) {
            float r = (i % 2 == 0) ? radius : innerRadius;
            double angle = Math.PI * i / 5 - Math.PI / 2;
            float x = (float) (centerX + r * Math.cos(angle));
            float y = (float) (centerY + r * Math.sin(angle));

            if (i == 0) {
                starPath.moveTo(x, y);
            } else {
                starPath.lineTo(x, y);
            }
        }
        starPath.close();

        canvas.drawPath(starPath, starPaint);
    }

    private void drawSpaceships(Canvas canvas) {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                BoardCell cell = gameBoard.getCell(x, y);
                if (cell != null) {
                    List<Spaceship> spaceships = cell.getSpaceships();
                    if (!spaceships.isEmpty()) {
                        drawSpaceshipsInCell(canvas, cell, spaceships);
                    }
                }
            }
        }
    }

    private void drawSpaceshipsInCell(Canvas canvas, BoardCell cell, List<Spaceship> spaceships) {
        int x = cell.getX() * cellSize;
        int y = cell.getY() * cellSize;

        // Adjust positions based on number of ships in the cell
        int shipSize = cellSize / 3;
        int padding = (cellSize - shipSize) / 4;

        switch (spaceships.size()) {
            case 1:
                // Center the single ship
                drawSpaceship(canvas,
                        x + cellSize/2 - shipSize/2,
                        y + cellSize/2 - shipSize/2,
                        shipSize,
                        spaceships.get(0));
                break;

            case 2:
                // Place ships side by side
                drawSpaceship(canvas,
                        x + padding,
                        y + cellSize/2 - shipSize/2,
                        shipSize,
                        spaceships.get(0));
                drawSpaceship(canvas,
                        x + cellSize - padding - shipSize,
                        y + cellSize/2 - shipSize/2,
                        shipSize,
                        spaceships.get(1));
                break;

            case 3:
                // Triangular arrangement
                drawSpaceship(canvas,
                        x + cellSize/2 - shipSize/2,
                        y + padding,
                        shipSize,
                        spaceships.get(0));
                drawSpaceship(canvas,
                        x + padding,
                        y + cellSize - padding - shipSize,
                        shipSize,
                        spaceships.get(1));
                drawSpaceship(canvas,
                        x + cellSize - padding - shipSize,
                        y + cellSize - padding - shipSize,
                        shipSize,
                        spaceships.get(2));
                break;

            default:
                // For more ships, just draw a generic indicator
                shipPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                canvas.drawCircle(x + cellSize/2, y + cellSize/2, cellSize/4, shipPaint);
                break;
        }
    }

    private void drawSpaceship(Canvas canvas, int x, int y, int size, Spaceship ship) {
        // Set color based on player
        PlayerColor color = ship.getColor();
        switch (color) {
            case BLUE:
                shipPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerBlue));
                break;
            case GREEN:
                shipPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerGreen));
                break;
            case RED:
                shipPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerRed));
                break;
            case YELLOW:
                shipPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerYellow));
                break;
        }

        // Draw a simple spaceship shape
        RectF shipRect = new RectF(x, y, x + size, y + size);
        canvas.drawRoundRect(shipRect, size/4, size/4, shipPaint);

        // Draw ship number
        Paint textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        textPaint.setTextSize(size * 0.6f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(ship.getShipNumber()),
                shipRect.centerX(),
                shipRect.centerY() + textPaint.getTextSize()/3, // Adjust for vertical centering
                textPaint);
    }

    private void drawCellSelection(Canvas canvas, BoardCell cell) {
        int x = cell.getX() * cellSize;
        int y = cell.getY() * cellSize;

        Rect cellRect = new Rect(x, y, x + cellSize, y + cellSize);
        canvas.drawRect(cellRect, selectionPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && cellClickListener != null) {
            int x = (int) (event.getX() / cellSize);
            int y = (int) (event.getY() / cellSize);

            if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
                BoardCell cell = gameBoard.getCell(x, y);
                if (cell != null) {
                    cellClickListener.onCellClick(cell);
                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * Interface for board cell click callbacks.
     */
    public interface BoardCellClickListener {
        void onCellClick(BoardCell cell);
    }
}
