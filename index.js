const express = require('express')
const app = express()
const port = process.env.PORT || 3000;


const bodyParser = require('body-parser');
app.use(bodyParser.json());

app.get('/', (req, res) => res.send('Running Tetris! WOW'))

app.post('/tetris', (req, res) => {

	var raw_in = req.body
	var seq = raw_in["tetrominoSequence"]
	var r = tetris(seq)

	var output = {}
	output["actions"] = r

	res.send(output);
})



function tetris(seqx) {

	/**** CONSTANTS ****/
	var BOARD_WIDTH = 10; // Num of Columns
	var BOARD_HEIGHT = 20; // Number of Rows
	var NUM_OF_COEFFICIENTS = 7; // Number of coefficients
	var NUM_EXTRA_PIECES = 0;
	var PIECES = [
	       [[1,1],         // 0: O
	        [1,1]],

	       [[1],           // 1: I1
	        [1],
	        [1],
	        [1]],

	       [[1, 1, 1, 1]],  // 2: I2

	       [[0,1],          // 3: J1
	        [0,1],
	        [1,1]],

	       [[1,0,0],        // 4: J2
	        [1,1,1]],

	       [[1,1],          // 5: J3
	        [1,0],
	        [1,0]],

	       [[1,1,1],        // 6: J4
	        [0,0,1]],

	       [[1,0],          // 7: L1
	        [1,0],
	        [1,1]],

	       [[0,0,1],        // 8: L2
	        [1,1,1]],

	       [[1,1],          // 9: L3
	        [0,1],
	        [0,1]],

	       [[1,1,1],        // 10: L4
	        [1,0,0]],

	       [[0,1,0],          // 11: T1
	        [1,1,1]],

	       [[1,0],        // 12: T2
	        [1,1],
	        [1,0]],

	       [[1,1,1],          // 13: T3
	        [0,1,0]],

	       [[0,1],        // 14: T4
	        [1,1],
	        [0,1]],

	       [[0,1,1],        // 15: S1
	        [1,1,0]],

	       [[1,0],          // 16: S2
	        [1,1],
	        [0,1]],

	       [[1,1,0],        // 17: Z1
	        [0,1,1]],

	       [[0,1],          // 18: Z2
	        [1,1],
	        [1,0]]//,
	      //
	      // [[1, 0],
	      //  [1, 1]]
	    ];
	var nameToIDMap = [[0],                     // O
	                   [1, 2],                  // I
	                   [3, 4, 5, 6],            // J
	                   [7, 8, 9, 10],           // L
	                   [11, 12, 13, 14],        // T
	                   [15, 16],                // S
	                   [17, 18]];               // Z
	                                            // X Initialized in Initialization.

	/**** Global Variables ****/
	// Board holds the state of the board as it is manipulated by the AI. Type: [char][char]
	var Board = [];



	/* can be improved by only checking rows that the last piece was inserted in !!
	 *  also by using something other than erase, insert.*/
	function removeClears(board, real) {
	    var clears = 0;
	    for(var i = 0; i < BOARD_HEIGHT; i++) {
	        var rowFull = true;
	        for(var j = 0; j < BOARD_WIDTH; j++) {
	            if(board[i][j] == -1){
	                rowFull = false;
	                break;
	            }
	        }
	        if(rowFull){
	            clears += 1;
	            board.splice(i, 1);
	            i--;
	            var newRow = [];
	            for (var k = 0; k < board[0].length; k++) {
	                newRow.push(-1);
	            }
	            board.splice(0, 0, newRow);
	        }
	    }
	    return clears;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	function dropPiece(board, pieceID, col, pieceName){

	    var piece = PIECES[pieceID];

	    for(var i = 0; i <= BOARD_HEIGHT - piece.length; i++){
	        if ( !canDropPiece(board, i, col, pieceID)) {
	            if(i == 0) {
	                // clearInterval(gameInterval);
	                return false;
	            }
	            else {
	                if (i <= 1) return false;
	                dropPieceAt(board, i - 1, col, pieceID, pieceName);
	                return true;
	            }
	        }
	    }

	    if(canDropPiece(board, BOARD_HEIGHT - piece.length, col, pieceID)){
	        dropPieceAt(board, BOARD_HEIGHT - piece.length, col, pieceID, pieceName);
	        return true;
	    }

	    return false;
	}

	// Returns true if piece can be dropped in row, col. If pieceName != 8, then makes the actual drop.
	function canDropPiece(board, row, col, pieceID){

	    var piece = PIECES[pieceID];

	    for(var i = 0; i < piece.length; i++) {
	        for (var j = 0; j < piece[i].length; j++) {
	            if (piece[i][j] && Board[row + i][col + j] != -1) return false;
	        }
	    }
	    return true;

	}

	// Drops piece at location row, col with character pieceChar, rotation rot
	function dropPieceAt(board, row, col, pieceID, pieceName){
	    var piece = PIECES[pieceID];

	    for(var i = 0; i < piece.length; i++){
	        for(var j = 0; j < piece[i].length; j++){
	            if(piece[i][j]) board[row + i][col + j] = pieceName;
	        }
	    }
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	function removePiece() {
	    for (var i = 0; i < Board.length; i++) {
	        for (var j = 0; j < Board[i].length; j++) {
	            if (Board[i][j] == 8) {
	                Board[i][j] = -1;
	                // Heights[j]--;
	            }
	        }
	    }
	}

	function gameOver() {
	    for (var i = 0; i < BOARD_WIDTH; i++) {
	        if (Board[0][i] + "" != -1) return true;
	    }
	    return false;
	}

	function generateRandomPiece() {
	    return Math.floor(Math.random() * nameToIDMap.length);
	}

	// var randString = "0 1 2 3 4 5 6";
	var randString = "OIJLTSZ";
	var randInput = randString.split(" ");
	var randIndex = 0;

	function initialize() {
	    for (var i = 0; i < BOARD_HEIGHT; i++) {
	        var newRow = [];
	        for (var j = 0; j < BOARD_WIDTH; j++) {
	            newRow.push(-1);
	        }
	        Board.push(newRow);
	    }
	    var arr = [];
	    for (var i = 19; i < PIECES.length; i++) {
	        arr.push(i);
	    }
	    if (arr.length > 0) nameToIDMap.push(arr); // X

	}

	function printBoard() {
	    var output = "____________\n";
	    for (var i = 0; i < BOARD_HEIGHT; i++) {
	        output += "|";
	        for (var j = 0; j < BOARD_WIDTH ; j++) {
	            if (Board[i][j] != -1) {
	                output += Board[i][j];
	            }
	            else {
	                output += ' ';
	            }
	        }
	        output += "|\n";
	    }
	    output += "____________";
	    console.log(output);
	}

	function calculateFitness(board, numCleared){
	    var totalHeight = 0,
	        maxHeight = 0,
	        numHoles = 0,
	        numBlockades = 0,
	        heightDifferences = 0,
	        firstHeight = 0,
	        lastHeight = 0;
	    // Calculate: firstHeight & lastHeight:
	    for (var i = 0; i < BOARD_HEIGHT; i++) {
	        if (Board[i][0] != -1) {
	            firstHeight = BOARD_HEIGHT - i;
	            break;
	        }
	    }
	    for (var i = 0; i < BOARD_HEIGHT; i++) {
	        if (Board[i][BOARD_WIDTH - 1] != -1) {
	            lastHeight = BOARD_HEIGHT - i;
	            break;
	        }
	    }

	    // Calculate: the rest:
	    // Count from the top
	    var heights = [BOARD_WIDTH];
	    var prevHeight = 0;
	    var currHeight = 0;
	    for (var i = 0; i < BOARD_WIDTH; i++) {
	        var startCountingHeight = false;
	        prevHeight = currHeight;
	        currHeight = 0;
	        var lastHole = -1;
	        for (var j = 0; j < BOARD_HEIGHT; j++) {
	            if (board[j][i] != -1) startCountingHeight = true;
	            if (startCountingHeight) {
	                currHeight++;
	                // Data: Count holes
	                if (board[j][i] == -1) {
	                    numHoles++;
	                    lastHole = j;
	                }
	            }
	        }
	        // Data: Count maximum column height
	        if (currHeight > maxHeight) maxHeight = currHeight;
	        // Data: Count difference in adjacent column heights.
	        if (i != 0) heightDifferences += Math.abs(currHeight - prevHeight);
	        // Data: Count total height.
	        totalHeight += currHeight;
	        // Data: Count blockades:
	        if (lastHole != -1) {
	            numBlockades += currHeight - (BOARD_HEIGHT - lastHole);
	        }
	        heights.push(currHeight);
	    }
	    var fitness =   coefficients[0] * heightDifferences +
	                    coefficients[1] * numHoles +
	                    coefficients[2] * (BOARD_HEIGHT - maxHeight) +
	                    coefficients[3] * numCleared +
	                    coefficients[4] * firstHeight +
	                    coefficients[5] * lastHeight +
	                    coefficients[6] * numBlockades;
	    // if (secondLevel) {
	    //     return fitness;
	    // }
	    // else {
	    //     secondLevel = true;
	    //
	    //     var pieceName = nextPiece;
	    //     var pieceIDs = nameToIDMap[pieceName];
	    //     var bestID = pieceIDs[0], bestCol = 0, bestScore = -999999;
	    //     for (var i = 0; i < pieceIDs.length; i++) {
	    //         for (var j = 0; j <= BOARD_WIDTH - PIECES[pieceIDs[i]][0].length; j++) {
	    //             // copy board
	    //             var board2 = [];
	    //             for (var a = 0; a < board.length; a++) {
	    //                 var newRow = [];
	    //                 for (var b = 0; b < board[0].length; b++) {
	    //                     newRow.push(board[a][b]);
	    //                 }
	    //                 board2.push(newRow);
	    //             }
	    //
	    //             if ( !dropPiece(board2, pieceIDs[i], j, 8) ) {
	    //                 continue;
	    //             }
	    //             var numCleared = removeClears(board2, true);
	    //             var score = calculateFitness(board2, numCleared);
	    //             if (score > bestScore) {
	    //                 bestScore = score;
	    //             }
	    //         }
	    //     }

	        return fitness; // + 0*bestScore;
	    // }
	}


	var currPiece;
	var secondLevel = false;

	function findBest(board, piece) {
	    var pieceName = piece;
	    var pieceIDs = nameToIDMap[pieceName];
	    var bestID = pieceIDs[0], bestCol = 0, bestScore = -999999;
	    for (var i = 0; i < pieceIDs.length; i++) {
	        for (var j = 0; j <= BOARD_WIDTH - PIECES[pieceIDs[i]][0].length; j++) {
	            // secondLevel = false;
	            // copy board
	            var board2 = [];
	            for (var a = 0; a < board.length; a++) {
	                var newRow = [];
	                for (var b = 0; b < board[0].length; b++) {
	                    newRow.push(board[a][b]);
	                }
	                board2.push(newRow);
	            }

	            if ( !dropPiece(board2, pieceIDs[i], j, 8) ) {
	                continue;
	            }

	            var numCleared = removeClears(board2, true);
	            var score = calculateFitness(board2, numCleared);
	            // removePiece();
	            if (score > bestScore) {
	                bestID = pieceIDs[i];
	                bestCol = j;
	                bestScore = score;
	            }
	        }
	    }

	    var result = []
	    result.push(bestID);
	    result.push(bestCol);
	    result.push(bestScore); // potential
	    return result;
	}

	function process(num){
	    if (num == 0) return 0;
	    if (num < 3) return num - 1;
	    if (num < 7) return num - 3;
	    if (num < 11) return (11 - num) % 4;
	    if (num < 15) return (num - 9) % 4;
	    if (num < 17) return num - 15;
	    return num - 17;
	}

	function runSimulation(seq) {
	    out = [];
	    for (var i = 0; i < seq.length; i++){
	        switch (seq[i]) {
	            case 'O':
	                currPiece = 0;
	                break;
	            case 'I':
	                currPiece = 1;
	                break;
	            case 'J':
	                currPiece = 2;
	                break;
	            case 'L':
	                currPiece = 3;
	                break;
	            case 'T':
	                currPiece = 4;
	                break;
	            case 'S':
	                currPiece = 5;
	                break;
	            case 'Z':
	                currPiece = 6;
	                break;
	        }

	        // copy board
	        var board = [];
	        for (var a = 0; a < Board.length; a++) {
	            var newRow = [];
	            for (var b = 0; b < Board[0].length; b++) {
	                newRow.push(Board[a][b]);
	            }
	            board.push(newRow);
	        }

	        var data1 = findBest(board, currPiece);

	        dropPiece(Board, data1[0], data1[1], currPiece);

	        removeClears(Board, true)

	        out[i] = 10*process(data1[0]) + data1[1];
	        // printBoard();
	    }
	    return out;
	}

	function main() {
	    initialize();
	    printBoard();
	}

	// Coefficients:
	//     coefficients[0] * heightDifferences +
	//     coefficients[1] * numHoles +
	//     coefficients[2] * (BOARD_HEIGHT - maxHeight) +
	//     coefficients[3] * numCleared +
	//     coefficients[4] * firstHeight +
	//     coefficients[5] * lastHeight +
	//     coefficients[6] * numBlockades;
	var coefficients = [
	    -0.192716,
	    -1,
	    0.00742194,
	    0.592781,
	    0.182602,
	    0.175692,
	    -0.0439177];

	main();


	//seqx only contains S, Z and T -> co[1] = -0.25
	//seqx only contains O and I -> co[1] = -1.3

	// var flag = 1;

	// for (var i = 0; i < 100; i++) {
	// 	if (seqx[i] != 'S' && seqx[i] != "Z" && seqx[i] != "T") {
	// 		flag = 0;
	// 		break;
	// 	}
	// }

	// coefficients[1] = (flag) ? -0.5 : coefficients[1];
	// coefficients[3] = (flag) ? 0.35 : coefficients[3];
	// coefficients[0] = (flag) ? -0.6 : coefficients[0];

	// flag = 1;
	// for (var i = 0; i < 100; i++) {
	// 	if (seqx[i] != 'O' && seqx[i] != "I"){
	// 		flag = 0;
	// 		break;
	// 	}
	// }

	// coefficients[1] = (flag) ? -1.3 : coefficients[1];
	// coefficients[1] = (seqx.length > 150) ? -1.0 : coefficients[1];
	// coefficients[3] = (seqx.length > 150) ? 0.35 : coefficients[3];

	console.log(seqx)
	return runSimulation(seqx);

}


app.listen(port, () => console.log(`Example app listening on port ${port}!`))
