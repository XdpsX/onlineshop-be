import PropTypes from 'prop-types'

const Sort = ({ curSelect, options, onSortChange }) => {
  return (
    <select
      value={curSelect}
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

Sort.propTypes = {
  curSelect: PropTypes.string,
  options: PropTypes.array,
  onSortChange: PropTypes.func,
}
export default Sort
