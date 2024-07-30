const Sort = ({ options, onSortChange }) => {
  return (
    <select
      onChange={(e) => onSortChange(e.target.value)}
      className=" ps-2 py-1 focus:border-indigo-500 outline-none bg-white border border-slate-700 rounded-md text-black"
    >
      {options.map((opt) => (
        <option key={opt.id} value={opt.value}>
          {opt.title}
        </option>
      ))}
    </select>
  )
}
export default Sort
