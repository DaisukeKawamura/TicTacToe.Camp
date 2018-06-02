package android.lifeistech.com.tictactoe;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //それぞれのプレイヤーがボードに置く画像
    public static final int[] PLAYER_IMAGES = {R.drawable.icon_maru, R.drawable.icon_bathu};

    //ターンの数を数える変数
    //プレイヤーも管理する　1:プレイヤー1　2:プレーヤー2
    public int turn;

    //ゲームの盤面
    //まだ誰も選択していないときは　-1
    public int[] gameBoard;

    //実際に見えているゲームの盤面:ボタンの配列
    public ImageButton[] boardButtons;

    //プレイヤーとターン表示用のTextView
    public TextView playerTextView;

    //勝敗表示用のTextView
    public TextView winnerTextView;

    //ボタンにつけたidをまとめておく
    public int[] buttonIds = {
            R.id.image_button_1,
            R.id.image_button_2,
            R.id.image_button_3,
            R.id.image_button_4,
            R.id.image_button_5,
            R.id.image_button_6,
            R.id.image_button_7,
            R.id.image_button_8,
            R.id.image_button_9,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTextView = (TextView) findViewById(R.id.player_text);
        winnerTextView = (TextView) findViewById(R.id.winner_text);

        boardButtons = new ImageButton[9];

        for (int i = 0; i < boardButtons.length; i++) {
            boardButtons[i] = (ImageButton) findViewById(buttonIds[i]);
        }

        init();
        setPlayer();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int Id = item.getItemId();

        if (Id == R.id.action_menu_reset){
            init();
            setPlayer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    //ゲームを新しくやり直す時に呼び出すメソッド
    public void init() {

        //編集の初期化
        turn = 1;
        //現在のゲームボードを初期化
        gameBoard = new int[boardButtons.length];
        for (int i = 0; i < boardButtons.length; i++) {
            //誰もそのマスに入っていない時は、-1を入れるようにする
            gameBoard[i] = -1;
            //ImageButtonで表示している画像を消す
            boardButtons[i].setImageBitmap(null);
        }
        winnerTextView.setVisibility(View.GONE);

    }

    public void setPlayer() {
        if (turn % 2 == 0) {
            playerTextView.setText("Player: ○(1)");
        } else {
            playerTextView.setText("Player: ×(2)");
        }
    }


    //プレイヤーがマスを選択した時の処理を行うためのメソッド
    public void tapImageButton(View v) {

        //勝敗が画面に出てきていない時だけ処理を行うようにする
        if (winnerTextView.getVisibility() == View.VISIBLE) return;

        //どのボタンが押されたのか取得する
        int tappedButtonPosition;
        int viewId = v.getId();

        if (viewId == R.id.image_button_1) {
            tappedButtonPosition = 0;
        } else if (viewId == R.id.image_button_2) {
            tappedButtonPosition = 1;
        } else if (viewId == R.id.image_button_3) {
            tappedButtonPosition = 2;
        } else if (viewId == R.id.image_button_4) {
            tappedButtonPosition = 3;
        } else if (viewId == R.id.image_button_5) {
            tappedButtonPosition = 4;
        } else if (viewId == R.id.image_button_6) {
            tappedButtonPosition = 5;
        } else if (viewId == R.id.image_button_7) {
            tappedButtonPosition = 6;
        } else if (viewId == R.id.image_button_8) {
            tappedButtonPosition = 7;
        } else {//viewId == R.id.image_button_9
            tappedButtonPosition = 8;
        }

        //まだ誰もそのマスを取っていないことを確認する
        if (gameBoard[tappedButtonPosition] == -1) {//誰もそのマスを取っていない
            //そのターンのプレイヤーの画像をおされたマスにセットする
            boardButtons[tappedButtonPosition].setImageResource(PLAYER_IMAGES[turn % 2]);
            gameBoard[tappedButtonPosition] = turn % 2;

            //勝敗がついたかどうかを判定する
            int judge = judgeGame();

            // judge の値が -1 だったら、勝敗がついていない
            // judge の値が 1 だったら、○ のプレイヤー勝利
            // judge の値が 0 だったら、× のプレイヤー勝利
            if (judge != -1) {//勝敗が決まった場合
                if (judge == 0) {
                    winnerTextView.setText("Game End \n Player: ○(1) \n Win");
                    winnerTextView.setTextColor(Color.BLUE);
                } else if (judge == 1) {//judge == 1 の時を想定
                    winnerTextView.setText("Game End \n Player: ×(2) \n Win");
                    winnerTextView.setTextColor(Color.RED);
                }
                winnerTextView.setVisibility(View.VISIBLE);

            } else {//全部場所が埋まっても勝敗が決まらなかった時（引き分けの時）
                if (turn >= gameBoard.length) {
                    winnerTextView.setText("Game End \n Draw");
                    winnerTextView.setTextColor(Color.YELLOW);
                    winnerTextView.setVisibility(View.VISIBLE);
                }
            }

            turn++;

            setPlayer();
        }

    }

    public int judgeGame(){
        for (int i = 0; i < 3; i++){//３列
            //横並びチェック
            if (isMarkedHorizontal(i)){
                return gameBoard[i * 3];//そのマスをとった人の値を返す
            }

            //縦並びチェック
            if (isMarkedVertical(i)){
                return gameBoard[i];
            }

        }

        //斜めのチェック
        if (isMarkedDiagonal()){
            return gameBoard[4];
        }

        return -1;//チェックした結果、まだ勝敗がついていない場合
    }

    //そのマスが誰かに取られている（-1ではない）
    //かつ、同じ人がとっている（i*3 と i*3+1 と i*3+2の値が同じ）
    public boolean isMarkedHorizontal(int i){
        if (gameBoard[i * 3] != -1 &&gameBoard[i * 3] == gameBoard[i * 3+1] && gameBoard[i * 3] == gameBoard[i * 3 + 2]){
            return true;
        }else {
            return false;
        }
    }


    public boolean isMarkedVertical(int i){
       if (gameBoard[i] != -1 && gameBoard[i] == gameBoard[i + 3] && gameBoard[i] == gameBoard[i + 6]){
           return true;
        }else {
           return false;
       }

    }

    public boolean isMarkedDiagonal(){
        if (gameBoard[0] != -1 && gameBoard[0] == gameBoard[4] && gameBoard[0] == gameBoard[8] ){
            return true;
        }else if (gameBoard[2] != -1 && gameBoard[2] == gameBoard[4] && gameBoard[2] == gameBoard[6]){
            return true;
        }else {
            return false;
        }
    }
}
