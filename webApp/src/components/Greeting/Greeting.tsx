import './Greeting.css';

import type {AnimationEvent} from 'react';
import {useState} from 'react';
import {JSLogo} from '../JSLogo/JSLogo.tsx';
import {Calculator, Greeting as KotlinGreeting} from 'shared';

export function Greeting() {
	const greeting = new KotlinGreeting();
	const calc = new Calculator();
	const [isVisible, setIsVisible] = useState<boolean>(false);
	const [isAnimating, setIsAnimating] = useState<boolean>(false);

	const handleClick = () => {
		console.log("Button clicked!");

		if (isVisible) {
			setIsAnimating(true);
		} else {
			setIsVisible(true);
		}

		console.log("calc.add(5, 3) = ", calc.add(5, 3));
		console.log("calc.subtract(5, 3) = ", calc.subtract(5, 3));
	};

	const handleAnimationEnd = (event: AnimationEvent<HTMLDivElement>) => {
		if (event.animationName === 'fadeOut') {
			setIsVisible(false);
			setIsAnimating(false);
		}
	};

	return (
		<div className="greeting-container">
			<button onClick={handleClick} className="greeting-button">
				Click me!
			</button>

			{isVisible && (
				<div className={isAnimating ? 'greeting-content fade-out' : 'greeting-content'}
				     onAnimationEnd={handleAnimationEnd}>
					<JSLogo/>
					<div>React: {greeting.greet()}</div>
				</div>
			)}
		</div>
	);
}