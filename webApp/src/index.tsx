import React, {useEffect, useState} from 'react';
import ReactDOM from 'react-dom/client';
import {Greeting} from './components/Greeting/Greeting.tsx';

const rootElement = document.getElementById('root');
if (!rootElement) throw new Error('Failed to find the root element');

ReactDOM.createRoot(rootElement).render(
	<React.StrictMode>
		<App/>
	</React.StrictMode>
);

function App() {
	const [ready, setReady] = useState(false)

	useEffect(() => {
		const id = setTimeout(() => setReady(true), 3000)
		return () => clearTimeout(id)
	}, [])

	return ready ? <Greeting/> : <div>로딩 중입니다...</div>;
}