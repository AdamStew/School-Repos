extern crate rand;
extern crate time;
extern crate chrono;

use std::io;
use std::io::Write;
use std::io::BufWriter;
use std::io::BufReader;
use std::io::BufRead;
use std::str::FromStr;
use rand::Rng;
use std::fs::File;
use std::path::Path;

struct Card {
	num: i32,
	suit: String,
}

fn create_deck() -> Vec<Card> {
	let mut deck: Vec<Card> = Vec::new();

	//Creating our deck of cards.
	for i in 1..14 {
		//Creating all the spades.
		let card: Card = Card {num: i, suit: String::from("Spades")};
		deck.push(card);
		
		//Creating all the clubs.
		let card: Card = Card {num: i, suit: String::from("Clubs")};
		deck.push(card);
		
		//Creating all the diamonds.
		let card: Card = Card {num: i, suit: String::from("Diamonds")};
		deck.push(card);
		
		//Creating all the hearts.
		let card: Card = Card {num: i, suit: String::from("Hearts")};
		deck.push(card);
	}
	
	//Shufling our cards by doing random swaps, this is the final result of our deck.
	for i in 1..100 {
		let shuffle_a = rand::thread_rng().gen_range(0, deck.len());
		let shuffle_b = rand::thread_rng().gen_range(0, deck.len());
		deck.swap(shuffle_a, shuffle_b);
	}
	
	return deck;
}

fn print_card(card: &Card) -> String {
	let mut print = String::new();

	if card.num == 1 { //Checking if an Ace
		print.push_str("an Ace of "); //Concat
		let suit_slice: &str = &*card.suit; //Converting from String to &str
		print.push_str(suit_slice); //Concat
		print.push_str(". \n"); //Concat
		return print;
	} else if card.num == 11 { //Checking if a Jack
		print.push_str("a Jack of "); //Concat
		let suit_slice: &str = &*card.suit; //Converting from String to &str
		print.push_str(suit_slice); //Concat
		print.push_str(". \n"); //Concat
		return print;
	} else if card.num == 12 { //Checking if a Queen
		print.push_str("a Queen of "); //Concat
		let suit_slice: &str = &*card.suit; //Converting from String to &str
		print.push_str(suit_slice); //Concat
		print.push_str(". \n"); //Concat
		return print;
	} else if card.num == 13 { //Checking if a King
		print.push_str("a King of "); //Concat
		let suit_slice: &str = &*card.suit; //Converting from String to &str
		print.push_str(suit_slice); //Concat
		print.push_str(". \n"); //Concat
		return print;
	} else { //Any number value
		print.push_str("a ");
		let num: String = card.num.to_string(); //Converting from int to String
		let num_slice: &str = &*num; //Converting from String to &str
		print.push_str(num_slice); //Concat
		print.push_str(" of "); //Concat
		let suit_slice: &str = &*card.suit; //Converting from String to &str
		print.push_str(suit_slice); //Concat
		print.push_str(". \n"); //Concat
		return print;
	}
}

fn print_deck(deck: &mut Vec<Card>) -> String{
	let mut print = String::from("DECK: ");
	
	for i in 0..deck.len() {
		let card = deck.remove(0);
		print.push_str("-> ");
		print.push_str(print_card(&card).trim());
		&deck.push(card);
	}
	
	return print;
}

fn discard(hand: &mut Vec<Card>) {
	while hand.len() > 3 {
		let card1 = hand.pop().unwrap();
		let card2 = hand.pop().unwrap();
		let card3 = hand.pop().unwrap();
		let card4 = hand.pop().unwrap();
							
		if card1.num == card4.num {
			println!("Your two cards have matching numbers!");
			println!("Discarding {}", print_card(&card4));
			println!("Discarding {}", print_card(&card3));
			println!("Discarding {}", print_card(&card2));
			println!("Discarding {}", print_card(&card1));
		} else if card1.suit == card4.suit {
			println!("Your two cards have matching suits!");
			println!("Discarding {}", print_card(&card3));
			println!("Discarding {}", print_card(&card2));
			hand.push(card4);
			hand.push(card1);
		} else {
			hand.push(card4);
			hand.push(card3);
			hand.push(card2);
			hand.push(card1);
			break;
		}
	}
}

fn discard_hidden(hand: &mut Vec<Card>) {
	while hand.len() > 3 {
		let card1 = hand.pop().unwrap();
		let card2 = hand.pop().unwrap();
		let card3 = hand.pop().unwrap();
		let card4 = hand.pop().unwrap();
							
		if card1.num == card4.num {
			//Not printing anymore.
		} else if card1.suit == card4.suit {
			hand.push(card4);
			hand.push(card1);
		} else {
			hand.push(card4);
			hand.push(card3);
			hand.push(card2);
			hand.push(card1);
			break;
		}
	}
}

fn print_last_four(hand: &mut Vec<Card>) -> String {
	let mut print = String::from("HAND: ");
	let size = hand.len();
	
	if hand.len() > 3 {
		for i in 0..4 {
			let card = hand.remove(size-4);
			print.push_str(print_card(&card).trim());
			hand.push(card);
		}
	} else {
		for i in 0..hand.len() {
			let card = hand.remove(0);
			print.push_str(print_card(&card).trim());
			hand.push(card);
		}
	}
	
	return print;
}

fn print_entire_hand(hand: &mut Vec<Card>) -> String {
	let mut print = String::from("HAND: ");

	for i in 0..hand.len() {
		let card = hand.remove(0);
		print.push_str(print_card(&card).trim());
		hand.push(card);
	}
	
	return print;
}

fn get_score(hand: &mut Vec<Card>) -> i32 {
	let mut new_score = 0;
	
	for i in 0..hand.len() {
		let num = hand.pop().unwrap().num;
		if num > 10 {
			new_score += 10;
		} else {
			new_score += num;
		}
	}
	
	return new_score;
}

fn check_leader_board(file: &str, new_score: i32) {

	let path = Path::new(file);
				
	if path.exists() {
		let mut broke = false;
		let mut few_ranks = false;
		let mut i = 0;
		let mut rank = 0;
		let mut name = String::new();
		let mut final_name = String::new();
		
		let file = File::open(path).unwrap();
		let reader = BufReader::new(file);
		
		for line in reader.lines() {
						
			let l = line.unwrap();
			let slice = &l[21..24];
			let old_score = i32::from_str(slice).unwrap();
			i += 1;
			rank += 1;
		
			if new_score < old_score {
				println!("Congrats on a high score!  Please enter your name (less than 20 total"); 
				println!(" characters long).");
							
							
							
				io::stdin().read_line(&mut name).expect("Failed to read line.");
				let name = name.trim();
							
				if name.len() > 20 {
					println!("Name is too long!  Cutting off after 20 characters.");
					let name = &name[0..20];
					final_name.push_str(name);
					final_name.push_str(" ");
				} else {
					final_name.push_str(name);
					while final_name.len() <= 20{
						final_name.push_str(" ");
					}	
				}
						
				broke = true;
				break;
			}
		}
					
		if !broke {
			few_ranks = true;
		}
				
		if i < 5 && few_ranks {
				
			println!("Congrats on a high score!  Please enter your name (less than 20 total"); 
			println!(" characters long).");
						
			io::stdin().read_line(&mut name).expect("Failed to read line.");
			let name = name.trim();
				
			if name.len() > 20 {
				println!("Name is too long!  Cutting off after 20 characters.");
				let name = &name[0..20];
				final_name.push_str(name);
				final_name.push_str(" ");
			} else {
				final_name.push_str(name);
				while final_name.len() <= 20{
					final_name.push_str(" ");
				}	
			}
			broke = true;
		}
					
		if broke {
			let mut i = 0;
						
			let mut rank_a = String::new();
			let mut rank_b = String::new();
			let mut rank_c = String::new();
			let mut rank_d = String::new();
						
			let file = File::open(path).unwrap();
			let reader = BufReader::new(file);
						
			for line in reader.lines() {
				let l = line.unwrap();
				if i == 0 {
					rank_a = l;
					rank_a.push_str("\r\n");
				} else if i == 1 {
					rank_b = l;
					rank_b.push_str("\r\n");
				} else if i == 2 {
					rank_c = l;
					rank_c.push_str("\r\n");
				} else if i == 3 {
					rank_d = l;
					rank_d.push_str("\r\n");
				}
				i += 1;
			}
						
			let file = File::create(path).unwrap();
			let mut writer = BufWriter::new(file);
				
			let new_score = format!("{:03}", new_score);
			let new_score = new_score.to_string();
			let mut new_rank = String::new();
			new_rank.push_str(&*final_name);
			new_rank.push_str(&new_score);
			let loc = chrono::Local::now();
			let mut new_rank = format!("{}  {}", new_rank, loc);
			new_rank.push_str("  \r\n");
					
						
						
			if rank == 1 && !few_ranks {
				writer.write_all(new_rank.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_a.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_b.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_c.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_d.as_bytes()).expect("Unable to write data.");	
			} else if rank == 2 && !few_ranks {
				writer.write_all(rank_a.as_bytes()).expect("Unable to write data.");
				writer.write_all(new_rank.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_b.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_c.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_d.as_bytes()).expect("Unable to write data.");
			} else if rank == 3 && !few_ranks {
				writer.write_all(rank_a.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_b.as_bytes()).expect("Unable to write data.");
				writer.write_all(new_rank.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_c.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_d.as_bytes()).expect("Unable to write data.");
			} else if rank == 4 && !few_ranks {
				writer.write_all(rank_a.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_b.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_c.as_bytes()).expect("Unable to write data.");
				writer.write_all(new_rank.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_d.as_bytes()).expect("Unable to write data.");
			} else {
				writer.write_all(rank_a.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_b.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_c.as_bytes()).expect("Unable to write data.");
				writer.write_all(rank_d.as_bytes()).expect("Unable to write data.");
				writer.write_all(new_rank.as_bytes()).expect("Unable to write data.");
			}
		}
	} else {
		println!("Leaderboard file not found.  Generating Leaders.txt.");
		println!("Congrats on a high score!  Please enter your name (less than 20 total"); 
		println!(" characters long).");
					
		let mut name = String::new();
		let mut final_name = String::new();
		
		io::stdin().read_line(&mut name).expect("Failed to read line.");
		let name = name.trim();
							
		if name.len() > 20 {
			println!("Name is too long!  Cutting off after 20 characters.");
			let name = &name[0..20];
			final_name.push_str(name);
			final_name.push_str(" ");
		} else {
			final_name.push_str(name);
			while final_name.len() <= 20{
				final_name.push_str(" ");
			}	
		}
					
		let file = File::create(path).unwrap();
		let mut writer = BufWriter::new(file);
			
					
		let new_score = format!("{:03}", new_score);
		let new_score = new_score.to_string();
		let mut new_rank = String::new();
		new_rank.push_str(&*final_name);
		new_rank.push_str(&new_score);
		new_rank.push_str("  \r\n");
					
		writer.write_all(new_rank.as_bytes()).expect("Unable to write data.");
	}
}

fn main() {

	println!("Welcome to 'Car-Solitaire'.  Please input 'Q' to quit the application,");
	println!(" 'L' to display the leaderboard, 'P' to play a user-watched game, and 'H'");
	println!("to play a hidden game.");

	
	
	let mut response = String::new();
	io::stdin().read_line(&mut response).expect("Failed to read line.");
	let mut broke = false;
	let file = "Leaders.txt";
	
	while response.trim() != "Q" && response.trim() != "q" {
		if response.trim() == "L" || response.trim() == "l" {
			let path = Path::new(file);
			if path.exists() {
				let file = File::open(path).unwrap();
				let reader = BufReader::new(file);
				
				for line in reader.lines() {
					let l = line.unwrap();
					println!("{}", l);
				}
				
			} else {
				let file = File::create(path);
				println!("No leaderboard file found.  Created blank file.");
			}
		} else if response.trim() == "P" || response.trim() == "p" {
			let mut deck: Vec<Card> = Vec::new();
			let mut hand: Vec<Card> = Vec::new();
			deck = create_deck();
			
			println!("Welcome to a user-watched game.  Please input 'C' to start/continue ");
			println!("the game, 'D' to display the current deck, and 'X' to exit the game.");
			
			while !deck.is_empty() {
				response = String::new();
				io::stdin().read_line(&mut response).expect("Failed to read line.");
				
				if response.trim() == "C" || response.trim() == "c" || response.trim() == "" {
					//Checking if we have at least 4 cards.
					if hand.len() < 4 {
						println!("Less than 4 cards found in your hand.  Drawing until reached 4 cards.");
						
						//Drawing cards until we have 4 cards.
						while hand.len() < 4 && !deck.is_empty() {
							let card = deck.pop().unwrap(); //Drawed card
							println!("Drew {}", print_card(&card));
							hand.push(card);
						}
						
						//Print hand.
						println!("{}", print_last_four(&mut hand));
						
						//Now discarding, will run until nothing left to discard, or fewer than 4 cards.
						discard(&mut hand);
						
					} else {
						let card = deck.pop().unwrap(); //Drawed card
						println!("Drew {}", print_card(&card));
						hand.push(card); //Adding to hand
						
						//Now discarding, will run until nothing left to discard, or fewer than 4 cards.
						discard(&mut hand);
						
						//Print hand.
						if !deck.is_empty() {
							println!("{}", print_last_four(&mut hand));
						}
						
					}
				} else if response.trim() == "D" || response.trim() == "d" {
					println!("{}", print_deck(&mut deck));
				} else if response.trim() == "X" || response.trim() == "x" {
					println!("Quitting game.");
					broke = true;
					break;
				}
			}
			if broke == false {
			
				println!("{}", print_entire_hand(&mut hand));
				
				//Get score.
				let new_score = get_score(&mut hand);
				println!("Score: {}", new_score);
				
				check_leader_board(file, new_score);
			}
		} else if response.trim() == "H" || response.trim() == "h" {
			let mut deck: Vec<Card> = Vec::new();
			let mut hand: Vec<Card> = Vec::new();
			deck = create_deck();
			
			println!("Welcome to a user-hidden game.  Please enjoy the ride.");
			
			while !deck.is_empty() {
				if hand.len() < 4 {
					while hand.len() < 4 && !deck.is_empty() {
						let card = deck.pop().unwrap(); //Drawed card
						hand.push(card);
					}
				
					//Now discarding, will run until nothing left to discard, or fewer than 4 cards.
					discard_hidden(&mut hand);
					
				} else {
						let card = deck.pop().unwrap(); //Drawed card
						hand.push(card); //Adding to hand
						
						discard_hidden(&mut hand);
					}
			}
			//Get score.
			let new_score = get_score(&mut hand);	
			println!("Score: {}", new_score);
			
			check_leader_board(file, new_score);
		}
	
		response = String::new();
		io::stdin().read_line(&mut response).expect("Failed to read line.");
	}

}
