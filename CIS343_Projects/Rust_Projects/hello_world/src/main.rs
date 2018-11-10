fn fun1(x: i32) -> i32 {
	let x = x + 1;
	fun2(x);
	return x;
}

fn fun2(x: i32) {
	println!("{}", x);
}

fn main() {
	let x = 5;
	fun1(x);
	println!("{}", x);
}